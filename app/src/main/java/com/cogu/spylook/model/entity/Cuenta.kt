package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cuentas", indices = [androidx.room.Index(value = ["link"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Anotable::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idPropietario"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus anotaciones también se eliminan
        )
    ]
)
data class Cuenta(
    @PrimaryKey(autoGenerate = true)
    @JvmField
    val idCuenta: Int,
    @JvmField
    val link: String,
    @JvmField
    var nickname: String,
    @JvmField
    var redSocial: String,
    @JvmField
    var idPropietario: Int
)