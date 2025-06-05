package com.cogu.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "cuentas", indices = [androidx.room.Index(value = ["link"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = AnotableEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idPropietario"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus cuentas también se eliminan
        )
    ]
)
class CuentaEntity(
    idAnotable: Int,
    nombre: String,
    @JvmField
    var link: String,
    @JvmField
    var redSocial: String,
    @JvmField
    var colorFoto: Int,
    @JvmField
    var idPropietario: Int
) : AnotableEntity(idAnotable, nombre)