package com.cogu.spylook.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.controller.GithubController
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.utils.decorators.RainbowTextViewDecorator
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class MainActivity : AppCompatActivity() {

    private val transitionEffect = Explode()
    private lateinit var adapter: PersonaCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapper: ContactoToCardItem
    private lateinit var dao: ContactoDAO
    private lateinit var githubController : GithubController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowTransitions()
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        applyWindowInsets()
        githubController = GithubController.getInstance()
        githubController.checkForUpdates(this)
        val database = AppDatabase.getInstance(this)
        dao = database!!.contactoDAO()!!
        mapper = Mappers.getMapper(ContactoToCardItem::class.java)

        setupButton()
        adapter = PersonaCardAdapter(emptyList(), this)
        setupRecyclerView()
        lifecycleScope.launch {
            loadContactData()
            adapter.notifyDataSetChanged()
        }
        setupSearchBar()
        applyRainbowDecorators()
    }

    private fun setupWindowTransitions() {
        window.requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = transitionEffect
        window.exitTransition = transitionEffect
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

    private fun setupButton() {
        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, NuevoContactoActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
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

    private fun setupSearchBar() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(
            TextWatcherSearchBar(searchEditText, recyclerView, adapter, this)
        )
    }

    private fun applyRainbowDecorators() {
        listOf(
            findViewById<TextView>(R.id.textUsuarios),
            findViewById<TextView>(R.id.textGrupos)
        ).forEach { textView ->
            RainbowTextViewDecorator(this, textView).apply()
        }
    }

    private suspend fun loadContactData() {
        val contactos = dao.getContactos()
        val cardItems = if (contactos.isEmpty()) {
            listOf(
                ContactoCardItem(
                    id = -1,
                    nombre = "Vaya...",
                    alias = "Qué vacio...",
                    colorFoto = 0,
                    clickable = false
                )
            )
        } else {
            contactos.mapNotNull { mapper.toCardItem(it) }
        }
        adapter = PersonaCardAdapter(cardItems, this)
    }

    override fun onResume() {
        super.onResume()
        runBlocking { loadContactData() }
        setupRecyclerView()
    }
}