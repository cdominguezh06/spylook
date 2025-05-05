package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.Suceso;

import java.util.List;

public class CausanteSuceso {
    @Embedded
    public Contacto contacto;

    @Relation(parentColumn = "id", entityColumn = "idCausante")
    public List<Suceso> sucesos;
}
