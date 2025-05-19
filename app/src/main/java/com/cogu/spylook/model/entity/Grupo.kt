package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
class Grupo(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idGrupo: Int = 0,
    @JvmField
    var nombre: String,
    @JvmField
    var idCreador: Int = 0,
) {
    constructor() : this(0, "", 0)
}
