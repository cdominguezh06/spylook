package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "cuenta_contacto_cross_ref",
    primaryKeys = ["idCuenta", "idContacto"],
    foreignKeys = [
        ForeignKey(
            entity = Cuenta::class,
            parentColumns = ["idCuenta"],
            childColumns = ["idCuenta"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Contacto::class,
            parentColumns = ["idContacto"],
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