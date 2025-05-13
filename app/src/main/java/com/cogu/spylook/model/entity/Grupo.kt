package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@Entity(tableName = "grupos")
@NoArgsConstructor
@AllArgsConstructor
class Grupo {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idGrupo: Int = 0
    @JvmField
    var nombre: String? = null
    @JvmField
    var idCreador: Int = 0
}
