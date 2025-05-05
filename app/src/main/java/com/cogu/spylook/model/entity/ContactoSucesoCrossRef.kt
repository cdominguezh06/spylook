package com.cogu.spylook.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {
        "idContacto",
        "idSuceso"
},
        foreignKeys = {
                @ForeignKey(
                        entity = Contacto.class,
                        parentColumns = "id",
                        childColumns = "idContacto",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Suceso.class,
                        parentColumns = "id",
                        childColumns = "idSuceso"
                )
        })
public class ContactoSucesoCrossRef {
    public int idContacto;
    public int idSuceso;
}
