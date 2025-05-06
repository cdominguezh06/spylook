package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.time.LocalDate
import java.time.LocalDateTime

@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "anotaciones")
class Anotacion {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @JvmField
    var fecha: String? = null
    @JvmField
    var titulo: String? = null
    @JvmField
    var descripcion: String? = null
    @JvmField
    var idContacto: Int = 0
}
