package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "contacto_amistad_cross_ref",
    primaryKeys = ["idContacto", "idAmigo"],
    foreignKeys = [
        ForeignKey(
            entity = Contacto::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Contacto::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idAmigo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idContacto"]),
        Index(value = ["idAmigo"])
    ]
)
data class ContactoAmistadCrossRef(
    var idContacto: Int,
    var idAmigo: Int
)