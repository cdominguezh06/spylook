package com.cogu.spylook.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.transition.Slide
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.GrupoCardAdapter
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.controller.GithubController
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.utils.decorators.RainbowTextViewDecorator
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarContacts
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarGroups
import com.cogu.spylook.view.contacts.NuevoContactoActivity
import com.cogu.spylook.view.groups.NuevoGrupoActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class MainActivity : AppCompatActivity() {

    private val transitionEffect = Slide()
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView
    private val contactoMapper: ContactoToCardItem = Mappers.getMapper(ContactoToCardItem::class.java)
    private val grupoMapper = Mappers.getMapper(GrupoToCardItem::class.java)
    private lateinit var githubController: GithubController
    private lateinit var searchEditText: EditText
    private lateinit var database: AppDatabase
    private var contactos = mutableListOf<Contacto>()
    private lateinit var intent : Intent
    private var grupos = mutableListOf<Grupo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowTransitions()
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        applyWindowInsets()
        githubController = GithubController.getInstance()
        githubController.checkForUpdates(this)
        database = AppDatabase.getInstance(this)!!
        searchEditText = findViewById<EditText>(R.id.searchEditText)
        setupButtons()
        intent = Intent(this, NuevoContactoActivity::class.java)
        adapter = PersonaCardAdapter(listOf(), this)
        runBlocking {
            loadDatas()
            val cardItems = if (contactos.isEmpty()) {
                listOf(
                    ContactoCardItem(
                        idAnotable = -1,
                        nombre = "Vaya...",
                        alias = "Qué vacío...",
                        colorFoto = 0,
                        clickable = false
                    )
                )
            } else {
                contactos.map { contactoMapper.toCardItem(it) }
            }
            adapter = PersonaCardAdapter(cardItems, context = this@MainActivity)
            adapter.notifyDataSetChanged()
        }
        setupRecyclerView()
        setupSearchBar(TextWatcherSearchBarContacts(searchEditText, recyclerView, this))
        applyRainbowDecorators()
    }

    private fun setupWindowTransitions() {
        window.requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = transitionEffect.apply {
            slideEdge = android.view.Gravity.BOTTOM
        }
        window.exitTransition = null

    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.button).setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
        }

        findViewById<ImageView>(R.id.imageViewGrupos).setOnClickListener {
            setupSearchBar(TextWatcherSearchBarGroups(searchEditText, recyclerView, this))
            val cardItems = if (grupos.isEmpty()) {
                listOf(
                    GrupoCardItem(
                        idGrupo = -1,
                        nombre = "Que vacío todo",
                        colorFoto = 0,
                        clickable = false
                    )
                )
            } else {
                grupos.map { grupoMapper.toCardItem(it) }
            }
            adapter = GrupoCardAdapter(cardItems, this)
            intent = Intent(this, NuevoGrupoActivity::class.java)
            setupRecyclerView()
        }

        findViewById<ImageView>(R.id.imageViewUsuarios).setOnClickListener {
            setupSearchBar(TextWatcherSearchBarContacts(searchEditText, recyclerView, this))
            val cardItems = if (contactos.isEmpty()) {
                listOf(
                    ContactoCardItem(
                        idAnotable = -1,
                        nombre = "Vaya...",
                        alias = "Que vacio todo",
                        colorFoto = 0,
                        clickable = false
                    )
                )
            } else {
                contactos.map { contactoMapper.toCardItem(it) }
            }
            adapter = PersonaCardAdapter(cardItems, this)
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(SpacingItemDecoration(this))
        }
    }

    private fun setupSearchBar(watcher: TextWatcher) {
        searchEditText.addTextChangedListener(
            watcher
        )
    }

    private fun applyRainbowDecorators() {
        listOf(
            findViewById<TextView>(R.id.textGrupos),
            findViewById<TextView>(R.id.textUsuarios),
        ).forEach { textView ->
            RainbowTextViewDecorator(this, textView).apply()
        }
    }

    private suspend fun loadDatas() {
        contactos = database.contactoDAO()!!.getContactos().toMutableList()
        grupos = database.grupoDAO()!!.getGrupos().toMutableList()
    }

    override fun onResume() {
        super.onResume()
        runBlocking { loadDatas() }
        val cardItems = if (contactos.isEmpty()) {
            listOf(
                ContactoCardItem(
                    idAnotable = -1,
                    nombre = "Vaya...",
                    alias = "Que vacio todo",
                    colorFoto = 0,
                    clickable = false
                )
            )
        } else {
            contactos.map { contactoMapper.toCardItem(it) }
        }
        adapter = PersonaCardAdapter(cardItems, this)
        setupRecyclerView()
    }
}