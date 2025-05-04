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
    private Contacto contacto;

    @Relation(
            parentColumn = "contacto_id",
            entityColumn = "grupo_id",
            associateBy = @Junction(ContactoGrupoCrossRef.class)
    )
    private List<Grupo> grupos;
}
