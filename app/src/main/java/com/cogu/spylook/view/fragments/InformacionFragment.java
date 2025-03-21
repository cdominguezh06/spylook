package com.cogu.spylook.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cogu.spylook.R;
import com.cogu.spylook.model.Contacto;


public class InformacionFragment extends Fragment {
    private TextView edadContent, nickContent,fechaContent, ciudadContent, estadoContent;
    private ImageView paisImg;

    private Contacto contacto;

    public InformacionFragment(Contacto contacto) {
        this.contacto = contacto;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_informacion, container, false);
        edadContent = fragment.findViewById(R.id.edadContent);
        nickContent = fragment.findViewById(R.id.nickContent);
        fechaContent = fragment.findViewById(R.id.fechaContent);
        ciudadContent = fragment.findViewById(R.id.ciudadContent);
        estadoContent = fragment.findViewById(R.id.estadoContent);
        paisImg = fragment.findViewById(R.id.imagenPais);

        edadContent.setText(String.valueOf(contacto.getEdad()));
        nickContent.setText(contacto.getNickMasConocido());
        fechaContent.setText(contacto.getFechaNacimiento().toString());
        ciudadContent.setText(contacto.getCiudad());
        estadoContent.setText(contacto.getEstado());
        return fragment;
    }
}
