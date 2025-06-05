package com.cogu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "anotables",
    indices = [androidx.room.Index(value = ["nombre"], unique = true)]

)
open class AnotableEntity(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var idAnotable: Int = 0,
    @JvmField
    var nombre: String
)
