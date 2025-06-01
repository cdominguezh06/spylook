package com.cogu.spylook.view.contacts

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.cogu.spylook.R
import com.cogu.spylook.adapters.slider.ContactSliderAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.common.MainActivity
import com.cogu.spylook.view.notification.NotificationHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers

class ContactoActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var title: TextView
    private lateinit var contactoDAO: ContactoDAO
    private lateinit var linearLayoutImagenEditar : LinearLayout
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
        title.isSelected = true
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)
        linearLayoutImagenEditar = findViewById<LinearLayout>(R.id.linearLayoutEditar)
        linearLayoutImagenEditar.setOnClickListener {
            val intent = Intent(this@ContactoActivity, NuevoContactoActivity::class.java)
            val activity = this@ContactoActivity
            intent.putExtra("idEdit", activity.intent.getIntExtra("id", -1))
            startActivity(intent)
        }
        lifecycleScope.launch {
            val contact = contactoDAO.findContactoById(intent.getIntExtra("id", 0))
            MainActivity.addRecentContact(Mappers.getMapper(ContactoToCardItem::class.java).toCardItem(contact), this@ContactoActivity)
            setupContactDetails(contact)
            setupViewPager(contact)
            NotificationHelper.lastContact = contact
        }
    }

    private fun setupContactDetails(contact: Contacto) {
        title.text = contact.nombre
        val image: ImageView = findViewById(R.id.imageView3)
        image.setImageResource(R.drawable.contact_icon)
        image.setColorFilter(contact.colorFoto, PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(contact: Contacto) {
        viewPager.adapter = ContactSliderAdapter(this, contact, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.TAB_INFO_TITLE)
                1 -> getString(R.string.TAB_FRIENDS_TITLE)
                2 -> "Grupos"
                3 -> "Sucesos"
                4 -> "Cuentas"
                else -> ""
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val contact = contactoDAO.findContactoById(intent.getIntExtra("id", 0))
            title.text = contact.nombre
            val currentPosition = viewPager.currentItem
            setupViewPager(contact)
            viewPager.post {
                viewPager.setCurrentItem(currentPosition, false)
            }

        }

    }

    override fun onStop() {
        super.onStop()
        NotificationHelper.cancel(this)
        NotificationHelper.lastContact?.let {
            NotificationHelper.showOpenContactNotification(this, it)
        }

    }
}