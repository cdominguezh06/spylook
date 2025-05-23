package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuentas", indices = [androidx.room.Index(value = ["link"], unique = true)])
data class Cuenta(
    @PrimaryKey(autoGenerate = true)
    @JvmField
    val idCuenta: Int,
    @JvmField
    val link: String,
    @JvmField
    var nickname: String,
    @JvmField
    var redSocial : String
)