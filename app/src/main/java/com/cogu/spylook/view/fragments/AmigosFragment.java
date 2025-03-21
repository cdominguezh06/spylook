package com.cogu.spylook.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class AmigosFragment extends Fragment {

    private Contacto contacto;
    private ContactoRepository repository;
    private ContactoToCardItem mapper;
    private Context context;
    public AmigosFragment(Contacto contacto, Context context) {
        this.contacto = contacto;
        repository = ContactoRepository.getInstance(context);
        mapper = Mappers.getMapper(ContactoToCardItem.class);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_amigos, container, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.recycleramigos);
        List<ContactoCardItem> amigos = repository.getAmigos(contacto).stream().map(mapper::toCardItem).collect(Collectors.toList());
        if (amigos.isEmpty()){
            amigos.add(new ContactoCardItem("Error", "No hay amigos", R.drawable.notfound, false));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new PersonaCardAdapter(amigos,context));
        return fragment;
    }
}
