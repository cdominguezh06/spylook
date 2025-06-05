package com.cogu.data.crossrefs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.GrupoEntity

@Entity(
    tableName = "contacto_grupo_cross_ref",
    primaryKeys = ["idContacto", "idGrupo"],
    foreignKeys = [
        ForeignKey(
            entity = ContactoEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GrupoEntity::class,
            parentColumns = ["idAnotable"],
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

