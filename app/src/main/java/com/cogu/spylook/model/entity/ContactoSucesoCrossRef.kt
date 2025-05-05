package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["idContacto", "idSuceso"
    ],
    foreignKeys = [ForeignKey(
        entity = Contacto::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("idContacto"),
        onDelete = ForeignKey.Companion.CASCADE
    ), ForeignKey(entity = Suceso::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idSuceso"))]
)
class ContactoSucesoCrossRef {
    var idContacto: Int = 0
    var idSuceso: Int = 0
}
