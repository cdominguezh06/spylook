package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "contacto_grupo_cross_ref",
    primaryKeys = ["idContacto", "idGrupo"],
    foreignKeys = [
        ForeignKey(
            entity = Contacto::class,
            parentColumns = ["idContacto"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["idGrupo"],
            childColumns = ["idGrupo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idContacto"]),
        Index(value = ["idGrupo"])
    ]
)
data class ContactoGrupoCrossRef(
    var idContacto: Int,
    var idGrupo: Int
)
