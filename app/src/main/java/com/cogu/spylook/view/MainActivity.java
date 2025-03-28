package com.cogu.spylook.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.PersonaCardAdapter;
import com.cogu.spylook.mappers.ContactoToCardItem;
import com.cogu.spylook.model.Contacto;
import com.cogu.spylook.model.cards.ContactoCardItem;
import com.cogu.spylook.repositories.ContactoRepository;

import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ContactoToCardItem mapper;
    private ContactoRepository repository;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().setDecorFitsSystemWindows(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = ContactoRepository.getInstance(this);
        mapper = Mappers.getMapper(ContactoToCardItem.class);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NuevoContactoActivity.class);
            startActivity(intent);
        });
        PersonaCardAdapter adapter = new PersonaCardAdapter(repository.getContactos().stream().map(mapper::toCardItem).collect(Collectors.toList()), this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        EditText text = findViewById(R.id.searchEditText);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (text.getText().toString().isEmpty()){
                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    recyclerView.setAdapter(adapter);
                }else{
                    List<ContactoCardItem> collect = repository.getContactos().stream()
                            .filter(i -> i.getNickMasConocido().toLowerCase().contains(text.getText().toString().toLowerCase()))
                            .map(mapper::toCardItem)
                            .collect(Collectors.toList());
                    if (collect.isEmpty()){
                        collect.add(new ContactoCardItem("", "Sin resultados", R.drawable.notfound, false));
                    }
                    PersonaCardAdapter newAdapter = new PersonaCardAdapter(collect, getBaseContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    recyclerView.setAdapter(newAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}