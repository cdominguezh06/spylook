package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef;
import com.cogu.spylook.model.entity.Suceso;

import java.util.List;

public class SucesosContactos {
    @Embedded
    public Suceso suceso;

    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = ContactoSucesoCrossRef.class,
                    parentColumn = "idSuceso",
                    entityColumn = "idContacto"))
    public List<Contacto> contactos;
}
