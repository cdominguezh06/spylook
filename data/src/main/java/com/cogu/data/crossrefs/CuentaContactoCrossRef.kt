package com.cogu.data.crossrefs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.CuentaEntity

@Entity(
    tableName = "cuenta_contacto_cross_ref",
    primaryKeys = ["idCuenta", "idContacto"],
    foreignKeys = [
        ForeignKey(
            entity = CuentaEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idCuenta"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContactoEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idContacto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idCuenta"]),
        Index(value = ["idContacto"])
    ]
)
data class CuentaContactoCrossRef(
    val idCuenta: Int,
    val idContacto: Int
)