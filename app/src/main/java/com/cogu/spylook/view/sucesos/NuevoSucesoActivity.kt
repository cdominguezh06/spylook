package com.cogu.spylook.view.sucesos

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.Slide
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
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
import com.cogu.spylook.R
import com.cogu.spylook.adapters.search.SingleContactCardSearchAdapter
import com.cogu.spylook.adapters.search.MultipleContactsCardSearchAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.model.utils.textWatchers.DateTextWatcher
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers

class NuevoSucesoActivity : AppCompatActivity() {

    private lateinit var recyclerCausante: RecyclerView
    private lateinit var recyclerImplicados: RecyclerView
    private lateinit var textNombreSuceso: EditText
    private lateinit var textDescripcionSuceso: EditText
    private lateinit var textFechaSuceso: EditText
    private lateinit var textLugarSuceso: EditText
    private lateinit var boton: Button
    private lateinit var imagen: ImageView
    private lateinit var db: AppDatabase
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    private var anotableOrigen: Int = -1
    var toEdit: Suceso? = null
    var causante = mutableListOf<ContactoCardItem>()
    var implicados = mutableListOf<ContactoCardItem>()
    var mapper: ContactoToCardItem = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_suceso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getInstance(this)!!
        anotableOrigen = intent.getIntExtra("id", -1)
        recyclerCausante = findViewById<RecyclerView>(R.id.recyclerCausante)
        recyclerImplicados = findViewById<RecyclerView>(R.id.recyclerImplicados)
        textNombreSuceso = findViewById<EditText>(R.id.editTextNombreSuceso)
        textDescripcionSuceso = findViewById<EditText>(R.id.editTextDescripcion)
        textFechaSuceso = findViewById<EditText>(R.id.editTextFechaSuceso).apply {
            addTextChangedListener(DateTextWatcher(this))
        }
        textLugarSuceso = findViewById<EditText>(R.id.editTextLugar)
        imagen = findViewById<ImageView>(R.id.imageView3)
        lifecycleScope.launch {
            if(intent.getIntExtra("idEdit", -1)!=-1){
                toEdit = AppDatabase.getInstance(this@NuevoSucesoActivity)!!.sucesoDAO()!!.findSucesoById(intent.getIntExtra("idEdit", -1))
            }
            toEdit?.let {
                textNombreSuceso.setText(toEdit?.nombre)
                textDescripcionSuceso.setText(toEdit?.descripcion)
                textFechaSuceso.setText(toEdit?.fecha!!)
                textLugarSuceso.setText(toEdit?.lugar)
                imagen.setImageResource(R.drawable.suceso_icon)
                imagen.setColorFilter(toEdit?.colorFoto!!, PorterDuff.Mode.MULTIPLY)
                val contactoDao = AppDatabase.getInstance(this@NuevoSucesoActivity)!!
                    .contactoDAO()!!
                val causanteEdit = mapper.toCardItem(contactoDao.findContactoById(toEdit?.idCausante!!))
                val implicadosEdit = AppDatabase.getInstance(this@NuevoSucesoActivity)!!
                    .sucesoDAO()!!.getRelacionesBySuceso(toEdit?.idAnotable!!).map {
                        mapper.toCardItem(contactoDao.findContactoById(it.idContacto))
                    }
                causante.add(causanteEdit)
                implicados.addAll(implicadosEdit)
            }
            val card = ContactoCardItem.DEFAULT_FOR_SEARCH
            causante.ifEmpty {causante.add(card)}
            implicados.add(card)
            var adapter = SingleContactCardSearchAdapter(
                causante,
                this@NuevoSucesoActivity,
                onClick = { cardItem, dialog, adapter ->
                    causante.clear()
                    causante.add(cardItem)
                    adapter.notifyItemRangeChanged(0, 1)
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("¿Desea eliminar al causante?")
                        .setPositiveButton("OK") { dialog, _ ->
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.dataSource = causante
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                0,
                                onEmptyCallback = {
                                    causante.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
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
            recyclerCausante.layoutManager = LinearLayoutManager(this@NuevoSucesoActivity)
            recyclerCausante.adapter = adapter
            recyclerAnimator = RecyclerViewAnimator(recyclerCausante, causante, adapter)
            recyclerImplicados.layoutManager = LinearLayoutManager(this@NuevoSucesoActivity)
            var adapterMiembros = MultipleContactsCardSearchAdapter(
                implicados,
                this@NuevoSucesoActivity,
                filter = {
                    val lista = mutableListOf<ContactoCardItem>()
                    lista.apply {
                        addAll(causante.filter { it.idAnotable != -1 })
                        addAll(implicados.filter { it.idAnotable != -1 })
                    }
                },
                onClick = { cardItem, dialog, adapter ->
                    val buscarCard =
                        implicados[implicados.size - 1]
                    implicados.removeAt(implicados.size - 1)
                    implicados.add(cardItem)
                    implicados.add(buscarCard)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("¿Desea eliminar al implicado ${cardItem.nombre} A.K.A ${cardItem.alias}?")
                        .setPositiveButton("OK") { dialog, _ ->
                            val index = implicados.indexOf(cardItem)
                            recyclerAnimator.dataSource = implicados
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                index,
                                onEmptyCallback = {
                                    implicados.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
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
            recyclerImplicados.adapter = adapterMiembros
        }

        boton = findViewById<Button>(R.id.buttonSiguiente)

        boton.setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            var alert = AlertDialog.Builder(this).setTitle("Error al crear el suceso")
            val nombreSuceso = textNombreSuceso.text.toString().trim()
            if (nombreSuceso.isEmpty()) {
                alert.setMessage("Por favor, ingresa un nombre para el suceso").show()
                return@setOnClickListener
            }
            if (causante.none { it.idAnotable != -1 }) {
                alert.setMessage("Debes agregar un causante").show()
                return@setOnClickListener
            }
            val origenIsInSuceso = implicados
                .filter { it.idAnotable == anotableOrigen }
                .toMutableList()
                .apply {
                    addAll(causante.filter {
                        it.idAnotable == anotableOrigen
                    })
                }
            origenIsInSuceso.ifEmpty {
                alert.setMessage("El contacto actual debe ser parte del suceso (causante o implicado)").show()
                return@setOnClickListener
            }
            val color = Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
            val fecha = textFechaSuceso.text.toString()
            val descripcion = textDescripcionSuceso.text.toString()
            val lugar = textLugarSuceso.text.toString()
            val suceso = Suceso(
                idAnotable = 0,
                nombre = nombreSuceso,
                fecha = fecha,
                lugar = lugar,
                descripcion = descripcion,
                colorFoto = color,
                idCausante = causante.first().idAnotable
            )
            toEdit?.let {
                AlertDialog.Builder(this@NuevoSucesoActivity)
                    .setTitle("Sobreescribir suceso")
                    .setMessage("¿Desea sobreescribir el suceso actual?")
                    .setPositiveButton("Confirmar") { dialog, _ ->
                        suceso.idAnotable = toEdit?.idAnotable!!
                        lifecycleScope.launch {
                            suceso.colorFoto = toEdit?.colorFoto!!
                            db.sucesoDAO()!!.updateSucesoAnotable(suceso)
                            db.sucesoDAO()!!.eliminarRelacionesPorSuceso(suceso.idAnotable)
                            insertarRelaciones(suceso.idAnotable)
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
                val sucesoId = db.sucesoDAO()!!.addSucesoAnotable(suceso).toInt()
                insertarRelaciones(sucesoId)
                finish()
            }

        }
    }

    suspend fun insertarRelaciones(sucesoId: Int){
        val relaciones = implicados
            .filter { it.idAnotable != -1 }
            .map { miembro ->
                ContactoSucesoCrossRef(
                    idSuceso = sucesoId,
                    idContacto = miembro.idAnotable
                )
            }
        relaciones.ifEmpty {
            finish()
            return
        }
        db.sucesoDAO()!!.insertarRelaciones(relaciones)
    }
}