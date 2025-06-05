package com.cogu.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "sucesos",
    foreignKeys = [
        ForeignKey(
            entity = AnotableEntity::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idCausante"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus sucesos también se eliminan
        )
    ]
)
class SucesoEntity(
    idAnotable: Int,
    nombre: String,
    var fecha: String,
    var lugar: String,
    var descripcion: String,
    var idCausante: Int,
    var colorFoto: Int
) : AnotableEntity(idAnotable, nombre)
