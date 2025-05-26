package com.cogu.spylook.view.groups

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.group.CreadorGrupoCardAdapter
import com.cogu.spylook.adapters.group.MiembrosGrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.Grupo
import kotlinx.coroutines.runBlocking

class NuevoGrupoActivity : AppCompatActivity() {

    companion object {
        var creador = mutableListOf<ContactoCardItem>()
        var miembros = mutableListOf<ContactoCardItem>()
    }

    private lateinit var recyclerCreadorGrupo: RecyclerView
    private lateinit var recyclerMiembrosGrupo: RecyclerView
    private lateinit var textNombreGrupo: EditText
    private lateinit var boton: Button
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getInstance(this)!!
        recyclerCreadorGrupo = findViewById<RecyclerView>(R.id.recyclerAgregarCreador)
        recyclerMiembrosGrupo = findViewById<RecyclerView>(R.id.recyclerAgregarMiembros)

        val card = ContactoCardItem(
            -1,
            "Pulsa para...",
            "Buscar",
            0,
            true
        )
        creador.add(card)
        miembros.add(card)
        var adapter = CreadorGrupoCardAdapter(creador, this)
        recyclerCreadorGrupo.layoutManager = LinearLayoutManager(this)
        recyclerCreadorGrupo.adapter = adapter

        recyclerMiembrosGrupo.layoutManager = LinearLayoutManager(this)
        var adapterMiembros = MiembrosGrupoCardAdapter(miembros, this)
        recyclerMiembrosGrupo.adapter = adapterMiembros
        textNombreGrupo = findViewById<EditText>(R.id.editTextNombre)
        boton = findViewById<Button>(R.id.buttonSiguiente)

        boton.setOnClickListener {

            var alert = AlertDialog.Builder(this).setTitle("Error al crear el grupo")
            val nombreGrupo = textNombreGrupo.text.toString().trim()
            if (nombreGrupo.isEmpty()) {
                alert.setMessage("Por favor, ingresa un nombre para el grupo").show()
                return@setOnClickListener
            }
            if (miembros.none { it.idAnotable != -1 }) {
                alert.setMessage("Debes tener al menos un miembro").show()
                return@setOnClickListener
            }
            if (miembros.none { it.idAnotable != -1 }) {
                alert.setMessage("Debes agregar un creador").show()
                return@setOnClickListener
            }
            val nuevoGrupo = Grupo(
                nombre = nombreGrupo,
                idCreador = creador[0].idAnotable // ID del creador (asumimos que solo hay uno)
            )
            runBlocking {
                val grupoId = db.grupoDAO()!!.addGrupo(nuevoGrupo).toInt()
                val relaciones = miembros
                    .filter { it.idAnotable != -1 }
                    .map { miembro ->
                        ContactoGrupoCrossRef(
                            idGrupo = grupoId,
                            idContacto = miembro.idAnotable
                        )
                    }
                db.grupoDAO()!!.insertarRelaciones(relaciones)
            }
            finish()
        }
    }
}