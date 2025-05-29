package com.cogu.spylook.view.accounts

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
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
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.model.utils.textWatchers.DateTextWatcher
import kotlinx.coroutines.launch

class NuevaCuentaActivity : AppCompatActivity() {

    private lateinit var recyclerPropietario: RecyclerView
    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var textNombreCuenta: EditText
    private lateinit var textLinkCuenta: EditText
    private lateinit var textRedSocialCuenta: EditText
    private lateinit var boton: Button
    private lateinit var db: AppDatabase
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    private var anotableOrigen: Int = -1
    var propietario = mutableListOf<ContactoCardItem>()
    var usuarios = mutableListOf<ContactoCardItem>()
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

        val card = ContactoCardItem.DEFAULT_FOR_SEARCH
        propietario.add(card)
        usuarios.add(card)
        var adapter = SingleContactCardSearchAdapter(
            propietario,
            this,
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
        recyclerPropietario.layoutManager = LinearLayoutManager(this)
        recyclerPropietario.adapter = adapter
        recyclerAnimator = RecyclerViewAnimator(recyclerPropietario, propietario, adapter)
        recyclerUsuarios.layoutManager = LinearLayoutManager(this)
        var adapterMiembros = MultipleContactsCardSearchAdapter(
            usuarios,
            this,
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
            val cuenta = Cuenta(
                idAnotable = 0,
                nombre = nombreCuenta,
                link = linkCuenta,
                redSocial = redSocial,
                colorFoto = color,
                idPropietario = propietario.first().idAnotable
            )
            lifecycleScope.launch {
                val cuentaId = db.cuentaDAO()!!.addCuentaWithAnotable(cuenta).toInt()
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
                    return@launch
                }

                db.cuentaDAO()!!.insertarRelaciones(relaciones)
                finish()
            }

        }
    }
}