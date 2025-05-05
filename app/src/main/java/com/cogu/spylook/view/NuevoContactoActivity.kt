package com.cogu.spylook.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cogu.spylook.R;
import com.cogu.spylook.bbdd.AppDatabase;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.unimplemented.DateTextWatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NuevoContactoActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextNick, editTextCumpleanos, editTextCiudad, editTextEstado, editTextPais;
    private Button siguiente;
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_contacto);
        getWindow().setDecorFitsSystemWindows(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNick = findViewById(R.id.editTextNick);
        editTextCumpleanos = findViewById(R.id.editTextCumpleanos);
        editTextCumpleanos.addTextChangedListener(new DateTextWatcher(editTextCumpleanos));
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextEstado = findViewById(R.id.editTextEstado);
        editTextPais = findViewById(R.id.editTextPais);
        siguiente = findViewById(R.id.buttonSiguiente);

        siguiente.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString();
            String nick = editTextNick.getText().toString();
            String cumpleanos = LocalDate.parse(editTextCumpleanos.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
            String ciudad = editTextCiudad.getText().toString();
            String estado = editTextEstado.getText().toString();
            String pais = editTextPais.getText().toString();
            Contacto contacto = new Contacto(nombre, nick, LocalDate.parse(cumpleanos), ciudad, estado, pais);
            db = AppDatabase.getInstance(this);
            executor.execute(() -> {
                db.contactoDAO().addContacto(contacto);
                finish();
            });
        });
    }
}