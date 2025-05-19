package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anotaciones")
data class Anotacion(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @JvmField
    var fecha: String,

    @JvmField
    var titulo: String,

    @JvmField
    var descripcion: String,

    @JvmField
    var idContacto: Int = 0
) {
    constructor(): this(0, "", "", "", 0)
}
