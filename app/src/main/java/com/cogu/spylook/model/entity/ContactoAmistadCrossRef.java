package com.cogu.spylook.model.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"contactoId", "amigoId"})
public class ContactoAmistadCrossRef {
    public int contactoId;
    public int amigoId;
}
