package com.cogu.spylook.view.contacts

import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.Slide
import android.view.Window
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
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
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
        image.setImageResource(R.drawable.contact_icon)
        image.setColorFilter(contact.colorFoto, PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(contact: Contacto) {
        viewPager.adapter = SliderAdapter(this, contact, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.TAB_FRIENDS_TITLE)
                1 -> getString(R.string.TAB_INFO_TITLE)
                else -> ""
            }
        }.attach()
    }
}