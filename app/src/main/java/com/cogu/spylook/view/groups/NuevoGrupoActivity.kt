package com.cogu.spylook.view.groups

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.search.CrearGrupoCardAdapter
import com.cogu.spylook.model.cards.ContactoCardItem

class NuevoGrupoActivity : AppCompatActivity() {

    private lateinit var recyclerCreadorGrupo: RecyclerView
    private lateinit var recyclerMiembrosGrupo: RecyclerView
    private lateinit var textNombreGrupo: EditText
    private lateinit var boton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerCreadorGrupo = findViewById<RecyclerView>(R.id.recyclerAgregarCreador)
        recyclerMiembrosGrupo = findViewById<RecyclerView>(R.id.recyclerAgregarMiembros)

        val card = ContactoCardItem(
            -1,
            "Buscar contacto",
            "Pulsa para buscar",
            0,
            true
        )
        var adapter = CrearGrupoCardAdapter(mutableListOf(card), this, 1)

    }
}