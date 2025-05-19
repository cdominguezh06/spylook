package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "sucesos")
class Suceso(
    @JvmField var fecha: LocalDate?,
    @JvmField var descripcion: String?,
    @JvmField var idCausante: Int
) {
    constructor() : this(null, null, 0)
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idSuceso: Int = 0
}
