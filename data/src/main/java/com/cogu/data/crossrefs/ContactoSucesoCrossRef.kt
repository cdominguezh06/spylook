package com.cogu.data.crossrefs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.SucesoEntity

@Entity(
    tableName = "contacto_suceso_cross_ref",
    primaryKeys = ["idContacto", "idSuceso"],
    foreignKeys = [
        ForeignKey(
            entity = ContactoEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SucesoEntity::class,
            parentColumns = ["idAnotable"],
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