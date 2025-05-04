package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"contacto_id", "grupo_id"})
public class ContactoGrupoCrossRef {
    public int contacto_id;
    public int grupo_id;
}
