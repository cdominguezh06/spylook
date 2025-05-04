package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef;
import com.cogu.spylook.model.entity.Grupo;

import java.util.List;

public class GruposContactos {
    @Embedded
    public Grupo grupo;

    @Relation(parentColumn = "idGrupo", entityColumn = "idContacto", associateBy = @Junction(ContactoGrupoCrossRef.class))
    public List<Contacto> contactos;
}
