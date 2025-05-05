package com.cogu.spylook.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.cogu.spylook.R
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.textWatchers.DateTextWatcher
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NuevoContactoActivity : AppCompatActivity() {
    private var editTextNombre: EditText? = null
    private var editTextNick: EditText? = null
    private var editTextCumpleanos: EditText? = null
    private var editTextCiudad: EditText? = null
    private var editTextEstado: EditText? = null
    private var editTextPais: EditText? = null
    private var siguiente: Button? = null
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_contacto)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
        editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        editTextNick = findViewById<EditText>(R.id.editTextNick)
        editTextCumpleanos = findViewById<EditText>(R.id.editTextCumpleanos)
        editTextCumpleanos!!.addTextChangedListener(DateTextWatcher(editTextCumpleanos))
        editTextCiudad = findViewById<EditText>(R.id.editTextCiudad)
        editTextEstado = findViewById<EditText>(R.id.editTextEstado)
        editTextPais = findViewById<EditText>(R.id.editTextPais)
        siguiente = findViewById<Button>(R.id.buttonSiguiente)

        siguiente!!.setOnClickListener(View.OnClickListener { v: View? ->
            val nombre = editTextNombre!!.getText().toString()
            val nick = editTextNick!!.getText().toString()
            val cumpleanos = LocalDate.parse(
                editTextCumpleanos!!.getText().toString(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ).toString()
            val ciudad = editTextCiudad!!.getText().toString()
            val estado = editTextEstado!!.getText().toString()
            val pais = editTextPais!!.getText().toString()
            val contacto = Contacto(nombre, nick, LocalDate.parse(cumpleanos), ciudad, estado, pais)
            db = AppDatabase.getInstance(this)
            runBlocking {
                db!!.contactoDAO().addContacto(contacto)
            }
            finish()

        })
    }
}