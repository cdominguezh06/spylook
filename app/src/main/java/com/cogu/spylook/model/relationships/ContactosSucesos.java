package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef;
import com.cogu.spylook.model.entity.Suceso;

import java.util.List;

public class ContactosSucesos {
    @Embedded
    private Contacto contacto;

    @Relation(parentColumn = "id", entityColumn = "id", associateBy = @Junction(ContactoSucesoCrossRef.class))
    private List<Suceso> sucesos;
}