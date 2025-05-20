package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "contacto_suceso_cross_ref",
    primaryKeys = ["idContacto", "idSuceso"],
    foreignKeys = [
        ForeignKey(
            entity = Contacto::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Suceso::class,
            parentColumns = ["idSuceso"],
            childColumns = ["idSuceso"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idContacto"]), // Índice para idContacto
        Index(value = ["idSuceso"]) // Índice para idSuceso
    ]

)
data class ContactoSucesoCrossRef(
    var idContacto: Int,
    var idSuceso: Int
)