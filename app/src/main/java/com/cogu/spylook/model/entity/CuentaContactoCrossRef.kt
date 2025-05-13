package com.cogu.spylook.model.entity

import androidx.room.Entity

@Entity(
    tableName = "cuenta_contacto_cross_ref",
    primaryKeys = ["cuentaId", "contactoId"]
)
data class CuentaContactoCrossRef(
    val idCuenta: Int,
    val idContacto: Int
)