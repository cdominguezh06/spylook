package com.cogu.spylook.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.R
import com.cogu.spylook.adapters.SliderAdapter
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.model.entity.Contacto
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import kotlinx.coroutines.runBlocking

class ContactoActivity : AppCompatActivity() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    private var title: TextView? = null
    private var db: AppDatabase? = null
    private var contactoDAO: ContactoDAO? = null

    private var fragmentActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_contacto)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
        db = AppDatabase.getInstance(this)
        contactoDAO = db!!.contactoDAO()
        title = findViewById<TextView>(R.id.contactoTitle)
        runBlocking {
            val byId: Contacto? = contactoDAO!!.findContactoById(intent.getIntExtra("id", 0))
            title!!.setText(byId!!.nombre)
            val image : ImageView = findViewById<View>(R.id.imageView3) as ImageView
            image.setImageResource(R.drawable.user_icon)
            image.setColorFilter(byId.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)

            viewPager = findViewById<ViewPager2>(R.id.pager)
            tabLayout = findViewById<TabLayout>(R.id.tabLayout)
            viewPager!!.setAdapter(SliderAdapter(fragmentActivity, byId, fragmentActivity))
            TabLayoutMediator(
                tabLayout!!, viewPager!!,
                TabConfigurationStrategy { tab: TabLayout.Tab?, position: Int ->
                    when (position) {
                        0 -> tab!!.setText("Información")
                        1 -> tab!!.setText("Amigos")
                    }
                }).attach()


        }
    }
}