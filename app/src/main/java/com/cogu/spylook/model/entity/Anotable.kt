package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "anotables",
    indices = [androidx.room.Index(value = ["nombre"], unique = true)]

)
open class Anotable(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idAnotable: Int = 0,
    @JvmField
    var nombre: String
)
