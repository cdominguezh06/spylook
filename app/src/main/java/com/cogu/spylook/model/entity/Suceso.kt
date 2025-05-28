package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idSuceso: Int,
    @JvmField var fecha: LocalDate,
    @JvmField var descripcion: String,
    @JvmField var idCausante: Int
)
