package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cogu.spylook.model.relations.CuentaConContactos

@Dao
interface CuentaDao {

    @Transaction
    @Query("SELECT * FROM cuentas WHERE id = :idCuenta")
    fun obtenerCuentaConContactos(idCuenta: Int): List<CuentaConContactos>

    @Transaction
    @Query("SELECT * FROM cuentas WHERE link = :link")
    fun obtenerCuentaConContactosPorLink(link: String): List<CuentaConContactos>

}