package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"idContacto", "idAmigo"})
public class ContactoAmistadCrossRef {
    public int idContacto;
    public int idAmigo;
}
