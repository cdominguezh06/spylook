package com.cogu.spylook.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.PersonaCardAdapter;
import com.cogu.spylook.bbdd.AppDatabase;
import com.cogu.spylook.mappers.ContactoToCardItem;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.textWatchers.TextWatcherSearchBar;
import com.cogu.spylook.model.decorators.RainbowTextViewDecorator;
import com.cogu.spylook.model.cards.ContactoCardItem;
import com.cogu.spylook.model.decorators.SpacingItemDecoration;
import com.cogu.spylook.DAO.ContactoDAO;

import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ContactoToCardItem mapper;
    private PersonaCardAdapter adapter;
    private RecyclerView recyclerView;
    private AppDatabase db;
    private ContactoDAO dao;
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
        db = AppDatabase.getInstance(this);
        dao = db.contactoDAO();
        mapper = Mappers.getMapper(ContactoToCardItem.class);
        prepareButton();
        prepareRecyclerView();
        EditText text = findViewById(R.id.searchEditText);
        text.addTextChangedListener(new TextWatcherSearchBar(text, recyclerView, adapter, this));

        TextView usuarios = findViewById(R.id.textUsuarios);
        TextView grupos = findViewById(R.id.textGrupos);

        RainbowTextViewDecorator decorator = new RainbowTextViewDecorator(this, usuarios);
        decorator.apply();
        decorator = new RainbowTextViewDecorator(this, grupos);
        decorator.apply();
    }

    private void prepareButton() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NuevoContactoActivity.class);
            startActivity(intent);
        });
    }

    private void prepareRecyclerView(){
        LiveData<List<Contacto>> contactLiveData = dao.getContactos();
        contactLiveData.observe(this, contactos -> {
            if (contactos.isEmpty()){
                adapter = new PersonaCardAdapter(List.of(new ContactoCardItem("Vaya...", "Qué vacio...", R.drawable.notfound, false)), this);
            }else{
                adapter = new PersonaCardAdapter(contactos.stream().map(mapper::toCardItem).collect(Collectors.toList()), this);
            }
            recyclerView = findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacingItemDecoration(this));
        });
    }
}