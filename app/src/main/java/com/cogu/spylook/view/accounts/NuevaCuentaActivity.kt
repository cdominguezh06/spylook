package com.cogu.spylook.view.accounts

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
import com.cogu.spylook.model.entity.CuentaEntity
import com.cogu.spylook.model.entity.CuentaContactoCrossRef
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers
import kotlin.collections.ifEmpty

class NuevaCuentaActivity : AppCompatActivity() {

    private lateinit var recyclerPropietario: RecyclerView
    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var textNombreCuenta: EditText
    private lateinit var textLinkCuenta: EditText
    private lateinit var textRedSocialCuenta: EditText
    private lateinit var boton: Button
    private lateinit var db: AppDatabase
    private lateinit var imagen: ImageView
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    private var anotableOrigen: Int = -1
    var toEdit: CuentaEntity? = null
    var propietario = mutableListOf<ContactoCardItem>()
    var usuarios = mutableListOf<ContactoCardItem>()
    var mapper: ContactoToCardItem = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getInstance(this)!!
        anotableOrigen = intent.getIntExtra("id", -1)
        recyclerPropietario = findViewById<RecyclerView>(R.id.recyclerPropietario)
        recyclerUsuarios = findViewById<RecyclerView>(R.id.recyclerUsuarios)
        textNombreCuenta = findViewById<EditText>(R.id.editTextNombreCuenta)
        textLinkCuenta = findViewById<EditText>(R.id.editTextLink)
        textRedSocialCuenta = findViewById<EditText>(R.id.editTextRedSocial)
        imagen = findViewById<ImageView>(R.id.imagenCuenta)
        lifecycleScope.launch {
            if(intent.getIntExtra("idEdit", -1)!=-1){
                toEdit = AppDatabase.getInstance(this@NuevaCuentaActivity)!!.cuentaDAO()!!.findCuentaById(intent.getIntExtra("idEdit", -1))
            }
            toEdit?.let {
                textNombreCuenta.setText(toEdit?.nombre)
                textLinkCuenta.setText(toEdit?.link)
                textRedSocialCuenta.setText(toEdit?.redSocial!!)
                imagen.setImageResource(R.drawable.account_icon)
                imagen.setColorFilter(toEdit?.colorFoto!!, PorterDuff.Mode.MULTIPLY)
                val contactoDao = AppDatabase
                    .getInstance(this@NuevaCuentaActivity)!!
                    .contactoDAO()!!
                val propietarioEdit = mapper.toCardItem(contactoDao.findContactoById(toEdit?.idPropietario!!))
                val usuariosEdit = AppDatabase.getInstance(this@NuevaCuentaActivity)!!
                    .cuentaDAO()!!
                    .findContactosByCuenta(toEdit?.idAnotable!!)
                    .map {
                        mapper.toCardItem(contactoDao.findContactoById(it.idContacto))
                    }
                propietario.add(propietarioEdit)
                usuarios.addAll(usuariosEdit)
            }
            val card = ContactoCardItem.DEFAULT_FOR_SEARCH
            propietario.ifEmpty {
                propietario.add(card)
            }
            usuarios.add(card)
            var adapter = SingleContactCardSearchAdapter(
                propietario,
                this@NuevaCuentaActivity,
                onClick = { cardItem, dialog, adapter ->
                    propietario.clear()
                    propietario.add(cardItem)
                    adapter.notifyItemRangeChanged(0, 1)
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("¿Desea eliminar al propietario de la cuenta?")
                        .setPositiveButton("OK") { dialog, _ ->
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.dataSource = propietario
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                0,
                                onEmptyCallback = {
                                    propietario.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
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
            recyclerPropietario.layoutManager = LinearLayoutManager(this@NuevaCuentaActivity)
            recyclerPropietario.adapter = adapter
            recyclerAnimator = RecyclerViewAnimator(recyclerPropietario, propietario, adapter)
            recyclerUsuarios.layoutManager = LinearLayoutManager(this@NuevaCuentaActivity)
            var adapterMiembros = MultipleContactsCardSearchAdapter(
                usuarios,
                this@NuevaCuentaActivity,
                filter = {
                    val lista = mutableListOf<ContactoCardItem>()
                    lista.apply {
                        addAll(propietario.filter { it.idAnotable != -1 })
                        addAll(usuarios.filter { it.idAnotable != -1 })
                    }
                },
                onClick = { cardItem, dialog, adapter ->
                    val buscarCard =
                        usuarios[usuarios.size - 1]
                    usuarios.removeAt(usuarios.size - 1)
                    usuarios.add(cardItem)
                    usuarios.add(buscarCard)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                },
                onLongClick = { cardItem, context, adapter, holder ->
                    if (cardItem.idAnotable == -1) false
                    AlertDialog.Builder(context)
                        .setTitle("¿Desea eliminar al usuario ${cardItem.nombre} A.K.A ${cardItem.alias}?")
                        .setPositiveButton("OK") { dialog, _ ->
                            val index = usuarios.indexOf(cardItem)
                            recyclerAnimator.dataSource = usuarios
                            recyclerAnimator.adapter = adapter
                            recyclerAnimator.deleteItemWithAnimation(
                                holder.itemView,
                                index,
                                onEmptyCallback = {
                                    usuarios.add(ContactoCardItem.DEFAULT_FOR_SEARCH)
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
            recyclerUsuarios.adapter = adapterMiembros
        }

        boton = findViewById<Button>(R.id.buttonSiguiente)

        boton.setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            var alert = AlertDialog.Builder(this).setTitle("Error al crear el suceso")
            val nombreCuenta = textNombreCuenta.text.toString().trim()
            val linkCuenta = textLinkCuenta.text.toString().trim()
            val redSocial = textRedSocialCuenta.text.toString().trim()
            nombreCuenta.ifEmpty{
                alert.setMessage("Por favor, ingresa un nombre para la cuenta").show()
                return@setOnClickListener
            }
            linkCuenta.ifEmpty{
                alert.setMessage("Por favor, ingresa un link para la cuenta").show()
                return@setOnClickListener
            }
            propietario.filter{ it.idAnotable != -1 }.ifEmpty {
                alert.setMessage("Debes agregar un propietario").show()
                return@setOnClickListener
            }

            redSocial.ifEmpty{
                alert.setMessage("Por favor, ingresa una red social para la cuenta").show()
                return@setOnClickListener
            }

            val origenIsInCuenta = usuarios
                .filter { it.idAnotable == anotableOrigen }
                .toMutableList()
                .apply {
                    addAll(propietario.filter {
                        it.idAnotable == anotableOrigen
                    })
                }
            origenIsInCuenta.ifEmpty {
                alert.setMessage("El contacto actual debe figurar en la cuenta (propietario o usuario)").show()
                return@setOnClickListener
            }
            val color = Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
            val cuentaEntity = CuentaEntity(
                idAnotable = 0,
                nombre = nombreCuenta,
                link = linkCuenta,
                redSocial = redSocial,
                colorFoto = color,
                idPropietario = propietario.first().idAnotable
            )
            toEdit?.let {
                AlertDialog.Builder(this@NuevaCuentaActivity)
                    .setTitle("Sobreescribir cuenta")
                    .setMessage("¿Desea sobreescribir la cuenta actual?")
                    .setPositiveButton("Confirmar") { dialog, _ ->
                        cuentaEntity.idAnotable = toEdit?.idAnotable!!
                        lifecycleScope.launch {
                            cuentaEntity.colorFoto = toEdit?.colorFoto!!
                            db.cuentaDAO()!!.updateCuentaAnotable(cuentaEntity)
                            db.cuentaDAO()!!.eliminarRelacionesPorCuenta(cuentaEntity.idAnotable)
                            insertarRelaciones(cuentaEntity.idAnotable)
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
                val cuentaId = db.cuentaDAO()!!.addCuentaWithAnotable(cuentaEntity).toInt()
                insertarRelaciones(cuentaId)
                finish()
            }

        }
    }

    suspend fun insertarRelaciones(cuentaId: Int){
        val relaciones = usuarios
            .filter { it.idAnotable != -1 }
            .map { miembro ->
                CuentaContactoCrossRef(
                    idCuenta = cuentaId,
                    idContacto = miembro.idAnotable
                )
            }
        relaciones.ifEmpty {
            finish()
            return
        }
        db.cuentaDAO()!!.insertarRelaciones(relaciones)
    }
}