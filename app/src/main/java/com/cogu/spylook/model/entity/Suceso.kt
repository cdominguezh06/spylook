package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(
    tableName = "sucesos",
    foreignKeys = [
        ForeignKey(
            entity = Anotable::class,
            parentColumns = ["idAnotable"],
            childColumns = ["idCausante"], // Relación con Anotable
            onDelete = ForeignKey.CASCADE // Si se elimina un Anotable, sus sucesos también se eliminan
        )
    ]
)
class Suceso(
    idAnotable: Int,
    nombre: String,
    @JvmField var fecha: String,
    @JvmField var lugar: String,
    @JvmField var descripcion: String,
    @JvmField var idCausante: Int,
    @JvmField var colorFoto: Int
) : Anotable(idAnotable, nombre)
