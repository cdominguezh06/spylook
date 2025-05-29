package com.cogu.spylook.view.sucesos

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
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.cogu.spylook.R
import com.cogu.spylook.adapters.slider.SucesoSliderAdapter
import com.cogu.spylook.dao.SucesoDAO
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.model.entity.Suceso
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class SucesoActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var title: TextView
    private lateinit var sucesoDAO: SucesoDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowTransitions()
        enableEdgeToEdge()
        setContentView(R.layout.activity_suceso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sucesoDAO = AppDatabase.Companion.getInstance(this)!!.sucesoDAO()!!
        title = findViewById(R.id.sucesoTitle)
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)

        lifecycleScope.launch {
            val suceso = sucesoDAO.findSucesoById(intent.getIntExtra("id", 0))
            setupSucesoDetails(suceso!!)
            setupViewPager(suceso)
        }
    }

    private fun setupSucesoDetails(suceso: Suceso) {
        title.text = suceso.nombre
        val image: ImageView = findViewById(R.id.imageView3)
        image.setImageResource(R.drawable.suceso_icon)
        image.setColorFilter(suceso.colorFoto, PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(suceso: Suceso) {
        viewPager.adapter = SucesoSliderAdapter(this, suceso, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.TAB_INFO_TITLE)
                1 -> "Implicados"
                2 -> "Anotaciones"
                else -> ""
            }
        }.attach()
    }

    private fun setupWindowTransitions() {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
    }
}