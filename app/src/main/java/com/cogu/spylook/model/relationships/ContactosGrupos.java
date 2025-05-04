package com.cogu.spylook.model.relationships;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef;
import com.cogu.spylook.model.entity.Grupo;

import java.util.List;

public class ContactosGrupos {
    @Embedded
    public Contacto contacto;

    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = ContactoGrupoCrossRef.class,
                    parentColumn = "idContacto", entityColumn = "idGrupo"))
    public List<Grupo> grupos;
}
