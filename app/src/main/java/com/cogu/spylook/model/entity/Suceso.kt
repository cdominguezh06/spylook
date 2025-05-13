package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import lombok.NoArgsConstructor
import java.time.LocalDate

@Entity(tableName = "sucesos")
@NoArgsConstructor
class Suceso(
    @JvmField var fecha: LocalDate?,
    @JvmField var descripcion: String?,
    @JvmField var idCausante: Int
) {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idSuceso: Int = 0
}
