package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef;

import java.util.List;

public class AmigosDeContacto {
    @Embedded
    public Contacto contacto;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = ContactoAmistadCrossRef.class,
                    parentColumn = "idContacto",
                    entityColumn = "idAmigo"
            )
    )
    public List<Contacto> amigos;
}
