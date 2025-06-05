package com.cogu.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "grupos",
    foreignKeys = [
        ForeignKey(
            entity = AnotableEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idCreador"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus grupos también se eliminan
        )
    ]
)
class GrupoEntity(
    idAnotable: Int = 0,
    nombre: String,
    @JvmField
    var colorFoto : Int,
    @JvmField
    var idCreador: Int = 0,
): AnotableEntity(idAnotable, nombre) {
    constructor() : this(0, "", 0)
}
