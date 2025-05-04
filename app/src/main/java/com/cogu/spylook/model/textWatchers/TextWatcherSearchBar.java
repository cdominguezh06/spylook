package com.cogu.spylook.model.textWatchers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.PersonaCardAdapter;
import com.cogu.spylook.bbdd.AppDatabase;
import com.cogu.spylook.mappers.ContactoToCardItem;
import com.cogu.spylook.model.cards.ContactoCardItem;

import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class TextWatcherSearchBar implements TextWatcher {

    private EditText text;
    private RecyclerView recyclerView;
    private PersonaCardAdapter adapter;
    private ContactoToCardItem mapper;
    private Context context;
    private AppDatabase db;
    public TextWatcherSearchBar(EditText text, RecyclerView recyclerView, PersonaCardAdapter adapter, Context context) {
        this.text = text;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
        this.mapper = Mappers.getMapper(ContactoToCardItem.class);
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (text.getText().toString().isEmpty()){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }else{
            List<ContactoCardItem> collect = db.contactoDAO().getContactos().stream()
                    .filter(i -> i.getAlias().toLowerCase().contains(text.getText().toString().toLowerCase()))
                    .map(mapper::toCardItem)
                    .collect(Collectors.toList());
            if (collect.isEmpty()){
                collect.add(new ContactoCardItem("", "Sin resultados", R.drawable.notfound, false));
            }
            PersonaCardAdapter newAdapter = new PersonaCardAdapter(collect, context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(newAdapter);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
