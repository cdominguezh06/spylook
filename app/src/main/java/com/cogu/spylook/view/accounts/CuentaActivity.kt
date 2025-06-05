package com.cogu.spylook.view.accounts

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
import com.cogu.data.dao.CuentaDao
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Cuenta
import com.cogu.spylook.R
import com.cogu.spylook.adapters.slider.CuentaSliderAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class CuentaActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var title: TextView
    private lateinit var cuentaDAO: CuentaDao
    private lateinit var linearLayoutImagenEditar : LinearLayout
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
        cuentaDAO = AppDatabase.Companion.getInstance(this)!!.cuentaDAO()!!
        title = findViewById(R.id.sucesoTitle)
        title.isSelected = true
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)

        linearLayoutImagenEditar = findViewById<LinearLayout>(R.id.linearLayoutEditar)
        linearLayoutImagenEditar.setOnClickListener {
            val intent = Intent(this@CuentaActivity, NuevaCuentaActivity::class.java)
            val activity = this@CuentaActivity
            intent.putExtra("idEdit", activity.intent.getIntExtra("id", -1))
            intent.putExtra("id", activity.intent.getIntExtra("idOrigen", -1))
            startActivity(intent)
        }
        lifecycleScope.launch {
            val cuenta = cuentaDAO.findCuentaById(intent.getIntExtra("id", 0))!!.toModel()
            setupCuentaDetails(cuenta)
            setupViewPager(cuenta)
        }
    }

    private fun setupCuentaDetails(cuenta: Cuenta) {
        title.text = cuenta.nombre
        val image: ImageView = findViewById(R.id.imageView3)
        image.setImageResource(R.drawable.account_icon)
        image.setColorFilter(cuenta.colorFoto, PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(cuenta: Cuenta) {
        viewPager.adapter = CuentaSliderAdapter(this, cuenta, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.TAB_INFO_TITLE)
                1 -> "Usuarios"
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val cuenta = cuentaDAO.findCuentaById(intent.getIntExtra("id", 0))!!.toModel()
            title.text = cuenta.nombre
            val currentPosition = viewPager.currentItem
            setupViewPager(cuenta)
            viewPager.post {
                viewPager.setCurrentItem(currentPosition, false)
            }

        }
    }
}