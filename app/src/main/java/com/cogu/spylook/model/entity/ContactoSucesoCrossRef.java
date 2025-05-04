package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"idContacto", "idSuceso"})
public class ContactoSucesoCrossRef {
    public int contactoId;
    public int sucesoId;
}
