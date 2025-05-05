package com.cogu.spylook.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.DAO.ContactoDAO
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.decorators.RainbowTextViewDecorator
import com.cogu.spylook.model.decorators.SpacingItemDecoration
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.textWatchers.TextWatcherSearchBar
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {
    private var mapper: ContactoToCardItem? = null
    private var adapter: PersonaCardAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var db: AppDatabase? = null
    private var dao: ContactoDAO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
        db = AppDatabase.getInstance(this)
        dao = db!!.contactoDAO()
        mapper = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
        prepareButton()

        runBlocking { loadData() }
        prepareRecyclerView()

        val text = findViewById<EditText>(R.id.searchEditText)
        text.addTextChangedListener(TextWatcherSearchBar(text, recyclerView, adapter, this))
        val usuarios = findViewById<TextView?>(R.id.textUsuarios)
        val grupos = findViewById<TextView?>(R.id.textGrupos)

        var decorator = RainbowTextViewDecorator(this, usuarios)
        decorator.apply()
        decorator = RainbowTextViewDecorator(this, grupos)
        decorator.apply()
    }

    private fun prepareButton() {
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, NuevoContactoActivity::class.java)
            startActivity(intent)
        })
    }

    private fun prepareRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setAdapter(adapter)
        if (recyclerView!!.itemDecorationCount <= 0) {
            recyclerView!!.addItemDecoration(SpacingItemDecoration(this))
        }
    }

    private suspend fun loadData(){
        var contactos = dao!!.getContactos()
        if (contactos.isEmpty()) {
            adapter = PersonaCardAdapter(
                listOf<ContactoCardItem?>(
                    ContactoCardItem(
                        "Vaya...",
                        "Qué vacio...",
                        R.drawable.notfound,
                        false
                    )
                ), this
            )
        } else {
            adapter = PersonaCardAdapter(
                contactos.stream().map({ contacto: Contacto? -> mapper!!.toCardItem(contacto) })
                    .collect(
                        Collectors.toList()
                    ), this
            )
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking { loadData() }
        prepareRecyclerView()
    }
}