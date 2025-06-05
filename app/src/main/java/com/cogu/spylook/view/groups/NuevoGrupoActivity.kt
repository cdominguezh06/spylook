package com.cogu.spylook.view.groups

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.crossrefs.ContactoGrupoCrossRef
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toEntity
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Grupo
import com.cogu.spylook.R
import com.cogu.spylook.adapters.search.SingleContactCardSearchAdapter
import com.cogu.spylook.adapters.search.MultipleContactsCardSearchAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import kotlinx.coroutines.launch
import kotlin.collections.ifEmpty

class NuevoGrupoActivity : AppCompatActivity() {

    var creador = mutableListOf<ContactoCardItem>()
    var miembros = mutableListOf<ContactoCardItem>()
    private lateinit var recyclerCreadorGrupo: RecyclerView
    private lateinit var recyclerMiembrosGrupo: RecyclerView
    private lateinit var textNombreGrupo: EditText
    private lateinit var boton: Button
    private lateinit var imagen: ImageView
    private lateinit var db: AppDatabase
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    var toEdit: Grupo? = null
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
        imagen = findViewById<ImageView>(R.id.imageView3)
        lifecycleScope.launch {
            if (intent.getIntExtra("idEdit", -1) != -1) {
                toEdit = AppDatabase.getInstance(this@NuevoGrupoActivity)!!.grupoDAO()!!
                    .findGrupoById(intent.getIntExtra("idEdit", -1))!!.toModel()
            }
            toEdit?.let {
                textNombreGrupo.setText(toEdit?.nombre)
                imagen.setImageResource(R.drawable.group_icon)
                imagen.setColorFilter(toEdit?.colorFoto!!, PorterDuff.Mode.MULTIPLY)
                val contactoDao = AppDatabase.getInstance(this@NuevoGrupoActivity)!!
                    .contactoDAO()!!
                val creadorEdit =
                    contactoDao.findContactoById(toEdit?.idCreador!!).toModel().toCardItem()
                val miembrosEdit = AppDatabase.getInstance(this@NuevoGrupoActivity)!!
                    .grupoDAO()!!.getRelacionesByGrupo(toEdit?.idAnotable!!).map {
                        contactoDao.findContactoById(it.idContacto).toModel().toCardItem()
                    }
                creador.add(creadorEdit)
                miembros.addAll(miembrosEdit)
            }
            val card = ContactoCardItem.DEFAULT_FOR_SEARCH
            creador.ifEmpty { creador.add(card) }
            miembros.add(card)
            var adapter = SingleContactCardSearchAdapter(
                creador,
                this@NuevoGrupoActivity,
                onClick = { cardItem, dialog, adapter ->
                    creador.clear()
                    creador.add(cardItem)
                    adapter.notifyItemRangeChanged(0, 1)
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("¿Desea eliminar al creador?")
                        .setPositiveButton("OK") { dialog, _ ->
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.dataSource = creador
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                0,
                                onEmptyCallback = {
                                    creador.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
                                },
                                afterDeleteCallBack = {
                                    adapter.notifyItemRangeChanged(0, 1)
                                    dialog.dismiss()
                                })
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                    true
                }
            )
            recyclerCreadorGrupo.layoutManager = LinearLayoutManager(this@NuevoGrupoActivity)
            recyclerCreadorGrupo.adapter = adapter
            recyclerAnimator = RecyclerViewAnimator(recyclerCreadorGrupo, creador, adapter)
            recyclerMiembrosGrupo.layoutManager = LinearLayoutManager(this@NuevoGrupoActivity)
            var adapterMiembros = MultipleContactsCardSearchAdapter(
                miembros,
                this@NuevoGrupoActivity,
                filter = {
                    val lista = mutableListOf<ContactoCardItem>()
                    lista.apply {
                        addAll(creador.filter { it.idAnotable != -1 })
                        addAll(miembros.filter { it.idAnotable != -1 })
                    }
                },
                onClick = { cardItem, dialog, adapter ->
                    val buscarCard =
                        miembros[miembros.size - 1]
                    miembros.removeAt(miembros.size - 1)
                    miembros.add(cardItem)
                    miembros.add(buscarCard)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("Retirar miembro")
                        .setMessage("¿Desea eliminar al miembro ${cardItem.nombre} A.K.A ${cardItem.alias}?")
                        .setPositiveButton("OK") { dialog, _ ->
                            val index = miembros.indexOf(cardItem)
                            recyclerAnimator.dataSource = miembros
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                index,
                                onEmptyCallback = {
                                    miembros.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
                                },
                                afterDeleteCallBack = {
                                    adapter.notifyItemRangeChanged(index, 1)
                                    dialog.dismiss()
                                })

                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                    true
                }
            )
            recyclerMiembrosGrupo.adapter = adapterMiembros
        }

        textNombreGrupo = findViewById<EditText>(R.id.editTextNombre)
        boton = findViewById<Button>(R.id.buttonSiguiente)

        boton.setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
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
            if (creador.none { it.idAnotable != -1 }) {
                alert.setMessage("Debes agregar un creador").show()
                return@setOnClickListener
            }
            val color = Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
            val nuevoGrupo = Grupo(0, nombreGrupo, color, creador.first().idAnotable)
            toEdit?.let {
                AlertDialog.Builder(this@NuevoGrupoActivity)
                    .setTitle("Sobreescribir grupo")
                    .setMessage("¿Desea sobreescribir el grupo actual?")
                    .setPositiveButton("Confirmar") { dialog, _ ->
                        nuevoGrupo.idAnotable = toEdit?.idAnotable!!
                        lifecycleScope.launch {
                            nuevoGrupo.colorFoto = toEdit?.colorFoto!!
                            db.grupoDAO()!!.updateGrupoAnotable(nuevoGrupo.toEntity())
                            db.grupoDAO()!!.eliminarRelacionesPorGrupo(nuevoGrupo.idAnotable)
                            insertarRelaciones(nuevoGrupo.idAnotable)
                            dialog.dismiss()
                            finish()
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val grupoId = db.grupoDAO()!!.addGrupoWithAnotable(nuevoGrupo.toEntity()).toInt()
                insertarRelaciones(grupoId)
                finish()
            }

        }
    }

    suspend fun insertarRelaciones(grupoId: Int) {
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
}