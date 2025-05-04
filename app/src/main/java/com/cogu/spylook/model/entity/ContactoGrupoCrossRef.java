package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"contacto_id", "grupo_id"})
public class ContactoGrupoCrossRef {
    public int contactoId;
    public int grupoId;
}
