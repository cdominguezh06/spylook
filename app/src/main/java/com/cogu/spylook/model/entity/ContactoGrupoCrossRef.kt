package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"idContacto", "idGrupo"})
public class ContactoGrupoCrossRef {
    public int idContacto;
    public int idGrupo;
}
