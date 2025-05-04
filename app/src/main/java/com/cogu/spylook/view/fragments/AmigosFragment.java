package com.cogu.spylook.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.PersonaCardAdapter;
import com.cogu.spylook.bbdd.AppDatabase;
import com.cogu.spylook.mappers.ContactoToCardItem;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.cards.ContactoCardItem;
import com.cogu.spylook.model.decorators.SpacingItemDecoration;
import com.cogu.spylook.DAO.ContactoDAO;
import com.cogu.spylook.model.relationships.AmigosDeContacto;

import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class AmigosFragment extends Fragment {

    private Contacto contacto;
    private ContactoToCardItem mapper;
    private Context context;
    private AppDatabase db;
    private ContactoDAO contactoDAO;
    public AmigosFragment(Contacto contacto, Context context) {
        this.contacto = contacto;
        db = AppDatabase.getInstance(context);
        contactoDAO = db.contactoDAO();
        mapper = Mappers.getMapper(ContactoToCardItem.class);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_amigos, container, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.recycleramigos);
        LiveData<AmigosDeContacto> amigosDeContacto = contactoDAO.getAmigosDeContacto(contacto.getId());
        amigosDeContacto.observe((LifecycleOwner) context, amigosDeContacto1 -> {
            List<ContactoCardItem> collect = amigosDeContacto1.amigos.stream()
                    .map(mapper::toCardItem)
                    .collect(Collectors.toList());
            if (collect.isEmpty()){
                collect.add(new ContactoCardItem("Error", "No hay amigos", R.drawable.notfound, false));
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PersonaCardAdapter(collect,context));
            recyclerView.addItemDecoration(new SpacingItemDecoration(context));
        });

        return fragment;
    }
}
