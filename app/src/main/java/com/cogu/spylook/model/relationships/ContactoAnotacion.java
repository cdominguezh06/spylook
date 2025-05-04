package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Anotacion;
import com.cogu.spylook.model.entity.Contacto;

import java.util.List;

public class ContactoAnotacion {
    @Embedded
    public Contacto contacto;

    @Relation(parentColumn = "id", entityColumn = "idContacto")
    public List<Anotacion> anotaciones;
}
