package com.cogu.spylook.view

import android.os.Bundle
import android.transition.Explode
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cogu.spylook.R
import com.cogu.spylook.adapters.SliderAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.model.entity.Contacto
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.runBlocking

class ContactoActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var title: TextView
    private lateinit var contactoDAO: ContactoDAO

    companion object {
        private const val TAB_INFO_TITLE = "Información"
        private const val TAB_FRIENDS_TITLE = "Amigos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowTransitions()
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_contacto)
        setupWindowInsets()
        initializeDatabase()
        setupUI()
    }

    private fun setupWindowTransitions() {
        window.requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Explode()
        window.exitTransition = Explode()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeDatabase() {
        contactoDAO = AppDatabase.getInstance(this)!!.contactoDAO()!!
    }

    private fun setupUI() {
        title = findViewById(R.id.contactoTitle)
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)

        runBlocking {
            val contact = contactoDAO.findContactoById(intent.getIntExtra("id", 0))
            setupContactDetails(contact)
            setupViewPager(contact)
        }
    }

    private fun setupContactDetails(contact: Contacto) {
        title.text = contact.nombre
        val image: ImageView = findViewById(R.id.imageView3)
        image.setImageResource(R.drawable.user_icon)
        image.setColorFilter(contact.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(contact: Contacto) {
        viewPager.adapter = SliderAdapter(this, contact, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> TAB_INFO_TITLE
                1 -> TAB_FRIENDS_TITLE
                else -> ""
            }
        }.attach()
    }
}