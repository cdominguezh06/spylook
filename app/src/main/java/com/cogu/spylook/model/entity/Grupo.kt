package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "grupos",
    foreignKeys = [
        ForeignKey(
            entity = Anotable::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idCreador"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus grupos también se eliminan
        )
    ]
)
class Grupo(
    idAnotable: Int = 0,
    nombre: String,
    @JvmField
    var colorFoto : Int,
    @JvmField
    var idCreador: Int = 0,
): Anotable(idAnotable, nombre) {
    constructor() : this(0, "", 0)
}
