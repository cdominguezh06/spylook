package com.cogu.spylook.model.entity

import androidx.room.Entity

@Entity(
    tableName = "grupos",
)
class Grupo(
    idAnotable: Int = 0,
    nombre: String,

    @JvmField
    var idCreador: Int = 0,
): Anotable(idAnotable, nombre) {
    constructor() : this(0, "", 0)
}
