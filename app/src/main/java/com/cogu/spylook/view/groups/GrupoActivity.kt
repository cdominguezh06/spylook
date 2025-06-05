package com.cogu.spylook.view.groups

import android.content.Intent
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
import com.cogu.spylook.adapters.slider.GroupSliderAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.dao.GrupoDAO
import com.cogu.spylook.model.entity.GrupoEntity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class GrupoActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var title: TextView
    private lateinit var grupoDAO: GrupoDAO
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
        grupoDAO = AppDatabase.getInstance(this)!!.grupoDAO()!!
    }

    private fun setupUI() {
        title = findViewById(R.id.contactoTitle)
        title.isSelected = true
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)
        linearLayoutImagenEditar = findViewById<LinearLayout>(R.id.linearLayoutEditar)
        linearLayoutImagenEditar.setOnClickListener {
            val intent = Intent(this@GrupoActivity, NuevoGrupoActivity::class.java)
            val activity = this@GrupoActivity
            intent.putExtra("idEdit", activity.intent.getIntExtra("id", -1))
            startActivity(intent)
        }
        lifecycleScope.launch {
            val grupo = grupoDAO.findGrupoById(intent.getIntExtra("id", 0))!!
            setupGroupDetails(grupo)
            setupViewPager(grupo)
        }
    }

    private fun setupGroupDetails(grupoEntity : GrupoEntity) {
        title.text = grupoEntity.nombre
        val image: ImageView = findViewById(R.id.imageView3)
        image.setImageResource(R.drawable.group_icon)
        image.setColorFilter(grupoEntity.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)
    }

    private fun setupViewPager(grupoEntity: GrupoEntity) {
        viewPager.adapter = GroupSliderAdapter(this, grupoEntity, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "MIEMBROS"
                1 -> "NOTAS"
                else -> ""
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val grupo = grupoDAO.findGrupoById(intent.getIntExtra("id", 0))
            title.text = grupo!!.nombre

            val currentPosition = viewPager.currentItem
            setupViewPager(grupo)
            viewPager.post {
                viewPager.setCurrentItem(currentPosition, false)
            }

        }
    }
}