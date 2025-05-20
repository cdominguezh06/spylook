package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "anotaciones",
    foreignKeys = [
        ForeignKey(
            entity = Anotable::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idAnotable"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus anotaciones también se eliminan
        )
    ]
)
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
    var idAnotable: Int = 0
) {
    constructor() : this(0, "", "", "", 0)
}

