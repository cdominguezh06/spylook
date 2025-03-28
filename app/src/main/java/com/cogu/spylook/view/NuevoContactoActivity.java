package com.cogu.spylook.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.SpinnerAdapter;
import com.cogu.spylook.model.Contacto;
import com.cogu.spylook.model.enums.PaisEnum;
import com.cogu.spylook.model.unimplemented.SpinnerableClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NuevoContactoActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextNick, editTextEdad, editTextCumpleanos, editTextCiudad, editTextEstado;
    private Spinner spinnerPais;
    private Button siguiente;
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
        editTextEdad = findViewById(R.id.editTextEdad);
        editTextCumpleanos = findViewById(R.id.editTextCumpleanos);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextEstado = findViewById(R.id.editTextEstado);
        spinnerPais = findViewById(R.id.spinnerPais);
        siguiente = findViewById(R.id.buttonSiguiente);
        PaisEnum[] paisEnums = PaisEnum.values();
        List<SpinnerableClass> collect = Arrays.stream(paisEnums).map(p -> new SpinnerableClass(p.getString(), p.getImage())).collect(Collectors.toList());
        List<Integer> ids = new ArrayList<>(Arrays.asList(R.layout.spinner_pais_view,R.id.imagePais));
        SpinnerAdapter<SpinnerableClass> adapter = new SpinnerAdapter<>(this, collect, ids);
        spinnerPais.setAdapter(adapter);

        siguiente.setOnClickListener(v -> {
            Contacto contacto = new Contacto();
            contacto.setNombre(editTextNombre.getText().toString());
            contacto.setNickMasConocido(editTextNick.getText().toString());
            contacto.setEdad(Integer.parseInt(editTextEdad.getText().toString()));
            contacto.setFechaNacimiento(LocalDate.parse(editTextCumpleanos.getText().toString()));
            contacto.setCiudad(editTextCiudad.getText().toString());
            contacto.setEstado(editTextEstado.getText().toString());
            contacto.setPais(((SpinnerableClass) spinnerPais.getSelectedItem()).getString());

        });
    }
}