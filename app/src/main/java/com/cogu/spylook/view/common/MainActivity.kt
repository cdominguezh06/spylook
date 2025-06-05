package com.cogu.spylook.view.common

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.transition.Slide
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter
import com.cogu.spylook.adapters.cards.GrupoCardAdapter
import com.cogu.spylook.controller.GithubController
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.utils.decorators.RainbowTextViewDecorator
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarContacts
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarGroups
import com.cogu.spylook.view.contacts.NuevoContactoActivity
import com.cogu.spylook.view.groups.NuevoGrupoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers
import androidx.core.graphics.createBitmap
import com.cogu.spylook.view.contacts.ContactoActivity

class MainActivity : AppCompatActivity() {

    private val transitionEffect = Slide()
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView
    private val contactoMapper: ContactoToCardItem =
        Mappers.getMapper(ContactoToCardItem::class.java)
    private val grupoMapper = Mappers.getMapper(GrupoToCardItem::class.java)
    private lateinit var githubController: GithubController
    private lateinit var searchEditText: EditText
    private lateinit var database: AppDatabase
    private var contactos = mutableListOf<ContactoCardItem>()
    private lateinit var intent: Intent
    private var grupos = mutableListOf<GrupoCardItem>()
    private lateinit var unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var toExecute: suspend CoroutineScope.() -> Unit

    companion object {
        val masRecientes: MutableList<ContactoCardItem> = mutableListOf()

        fun addRecentContact(contact: ContactoCardItem, context: Context) {
            if (masRecientes.size > 3) {
                masRecientes.removeAt(0)
            }
            masRecientes.add(contact)
            val shorcuts = masRecientes.map { c ->
                val image =
                    AppCompatResources.getDrawable(context, R.drawable.contact_icon)?.mutate()
                image?.setTint(c.colorFoto)
                image?.setTintMode(PorterDuff.Mode.MULTIPLY)
                val bitmap = createBitmap(100, 100)
                val canvas = Canvas(bitmap)
                image?.setBounds(0, 0, canvas.width, canvas.height)
                image?.draw(canvas)
                ShortcutInfo.Builder(context, c.idAnotable.toString())
                    .setShortLabel(c.nombre)
                    .setLongLabel(c.alias)
                    .setIcon(Icon.createWithBitmap(bitmap))
                    .setIntent(Intent(context, ContactoActivity::class.java).apply {
                        putExtra("id", c.idAnotable)
                        action = Intent.ACTION_VIEW
                    })
                    .build()
            }
            context.getSystemService(ShortcutManager::class.java).dynamicShortcuts = shorcuts
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowTransitions()
        this.enableEdgeToEdge()

        githubController = GithubController.Companion.getInstance()
        unknownAppsPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (this@MainActivity.packageManager.canRequestPackageInstalls()) {
                githubController.checkForUpdates(this, unknownAppsPermissionLauncher)
            } else {
                Toast.makeText(this@MainActivity, "Permiso denegado.", Toast.LENGTH_SHORT).show()
            }
        }
        githubController.checkForUpdates(this, unknownAppsPermissionLauncher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        applyWindowInsets()
        database = AppDatabase.Companion.getInstance(this)!!
        searchEditText = findViewById<EditText>(R.id.searchEditText)
        setupButtons()
        intent = Intent(this, NuevoContactoActivity::class.java)
        adapter = ContactoCardAdapter(mutableListOf(), this)
        toExecute = {
            loadContacts()
            adapter = ContactoCardAdapter(contactos, context = this@MainActivity)
            adapter.notifyItemRangeChanged(0, contactos.size - 1)
            setupRecyclerView()
            setupSearchBar(
                TextWatcherSearchBarContacts(
                    searchEditText,
                    recyclerView,
                    this@MainActivity
                )
            )
            applyRainbowDecorators()
        }
        lifecycleScope.launch(block = toExecute)
    }

    private fun setupWindowTransitions() {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = transitionEffect.apply {
            slideEdge = Gravity.BOTTOM
        }
        window.exitTransition = null

    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.button).setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
        }

        findViewById<ImageView>(R.id.imageViewGrupos).setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            toExecute = {
                setupSearchBar(
                    TextWatcherSearchBarGroups(
                        searchEditText,
                        recyclerView,
                        this@MainActivity
                    )
                )
                loadGroups()
                adapter = GrupoCardAdapter(grupos, this@MainActivity)
                intent = Intent(this@MainActivity, NuevoGrupoActivity::class.java)
                setupRecyclerView()
            }
            lifecycleScope.launch(block = toExecute)
        }

        findViewById<ImageView>(R.id.imageViewUsuarios).setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            toExecute = {
                setupSearchBar(
                    TextWatcherSearchBarContacts(
                        searchEditText,
                        recyclerView,
                        this@MainActivity
                    )
                )
                loadContacts()
                adapter = ContactoCardAdapter(contactos, this@MainActivity)
                intent = Intent(this@MainActivity, NuevoContactoActivity::class.java)
                setupRecyclerView()
            }
            lifecycleScope.launch(block = toExecute)
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

    private suspend fun loadContacts() {
        contactos = database.contactoDAO()!!.getContactos()
            .map { contactoMapper.toCardItem(it) }.toMutableList()
        contactos.ifEmpty { contactos.add(ContactoCardItem.Companion.DEFAULT_FOR_EMPTY_LIST) }
    }

    private suspend fun loadGroups() {
        grupos = database.grupoDAO()!!.getGrupos()
            .map { grupoMapper.toCardItem(it) }.toMutableList()
        grupos.ifEmpty { grupos.add(GrupoCardItem.Companion.DEFAULT_FOR_EMPTY_LIST) }
    }

    override fun onResume() {
        super.onResume()
        searchEditText.clearFocus()
        searchEditText.text.clear()
        lifecycleScope.launch(block = toExecute)
    }

}