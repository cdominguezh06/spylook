package com.cogu.spylook.view.contacts

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.utils.converters.DateConverters
import com.cogu.spylook.model.utils.textWatchers.DateTextWatcher
import com.cogu.spylook.view.widget.CustomProvider
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NuevoContactoActivity() : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var nickEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var stateEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var nextButton: Button
    private lateinit var imagen : ImageView
    private lateinit var database: AppDatabase
    var toEdit: Contacto? = null
    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowTransitions()
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_contacto)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initViews()
        setupNextButtonClickListener()
    }

    private fun initWindowTransitions() {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
    }

    private fun initViews() {
        nameEditText = findViewById(R.id.editTextNombre)
        nickEditText = findViewById(R.id.editTextNick)
        birthdayEditText = findViewById<EditText>(R.id.editTextCumpleanos).apply {
            addTextChangedListener(DateTextWatcher(this))
        }
        cityEditText = findViewById(R.id.editTextCiudad)
        stateEditText = findViewById(R.id.editTextEstado)
        countryEditText = findViewById(R.id.editTextPais)
        nextButton = findViewById(R.id.buttonSiguiente)
        imagen = findViewById<ImageView>(R.id.imageView3)

        lifecycleScope.launch {
            if(intent.getIntExtra("idEdit", -1)!=-1){
                toEdit = AppDatabase.getInstance(this@NuevoContactoActivity)!!.contactoDAO()!!.findContactoById(intent.getIntExtra("idEdit", -1))
            }
            toEdit?.let {
                nameEditText.setText(toEdit?.nombre)
                nickEditText.setText(toEdit?.alias)
                birthdayEditText.setText(DateConverters.toCustomString(toEdit?.fechaNacimiento!!,DATE_FORMATTER))
                cityEditText.setText(toEdit?.ciudad)
                stateEditText.setText(toEdit?.estado)
                countryEditText.setText(toEdit?.pais)
                imagen.setImageResource(R.drawable.contact_icon)
                imagen.setColorFilter(toEdit?.colorFoto!!, PorterDuff.Mode.MULTIPLY)
            }
        }

        val mainView: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupNextButtonClickListener() {
        nextButton.setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val contact = createContactFromInput()
            toEdit?.let {
                database = AppDatabase.getInstance(this@NuevoContactoActivity)!!
                AlertDialog.Builder(this@NuevoContactoActivity)
                    .setTitle("Sobreescribir contacto")
                    .setMessage("¿Desea sobreescribir el contacto actual?")
                    .setPositiveButton("Confirmar") { dialog, _ ->
                        val contacto = createContactFromInput()
                        contacto.colorFoto = it.colorFoto
                        lifecycleScope.launch {
                            contacto.idAnotable = toEdit!!.idAnotable
                            database.contactoDAO()!!.updateContactoWithAnotable(contacto)
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
                database = AppDatabase.getInstance(this@NuevoContactoActivity)!!
                database.contactoDAO()!!.findContactoByName(contact.nombre)?.let {
                    AlertDialog.Builder(this@NuevoContactoActivity)
                        .setTitle("Error al crear el contacto")
                        .setMessage("Ya existe un contacto con ese nombre")
                        .show()
                    return@launch
                }
                database.contactoDAO()!!.addContactoWithAnotable(contact)
                notifyWidgetUpdate()
                finish()
            }
        }
    }

    private fun createContactFromInput(): Contacto {
        val name = nameEditText.text.toString()
        val nick = nickEditText.text.toString()
        val birthday = LocalDate.parse(birthdayEditText.text.toString(), DATE_FORMATTER)
        val city = cityEditText.text.toString()
        val state = stateEditText.text.toString()
        val country = countryEditText.text.toString()
        val color = Color.rgb((0..255).random(), (0..255).random(), (0..255).random())

        return Contacto(
            nombre = name,
            alias = nick,
            fechaNacimiento = birthday,
            ciudad = city,
            estado = state,
            pais = country,
            colorFoto = color
        )
    }

    private fun notifyWidgetUpdate() {
        val intent = Intent(this, CustomProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this)
            .getAppWidgetIds(ComponentName(this, CustomProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}