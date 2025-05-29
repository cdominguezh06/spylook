package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef
import com.cogu.spylook.model.relations.CuentaConContactos

@Dao
interface CuentaDao {

    @Insert
    suspend fun addAnotable(anotable: Anotable): Long

    @Insert
    suspend fun insert(cuenta: Cuenta)

    @Query("SELECT * FROM cuentas WHERE idAnotable = :id")
    suspend fun findCuentaById(id: Int): Cuenta?

    @Query("SELECT * FROM cuentas WHERE idPropietario = :id")
    suspend fun findCuentasByPropietario(id: Int): List<Cuenta>

    @Transaction
    suspend fun addCuentaWithAnotable(cuenta: Cuenta) : Long {
        val idAnotable = addAnotable(
            Anotable(
                idAnotable = cuenta.idAnotable,
                nombre = cuenta.nombre
            )
        )
        cuenta.idAnotable = idAnotable.toInt()
        insert(cuenta)
        return idAnotable
    }

    @Transaction
    @Query("SELECT * FROM cuentas WHERE idAnotable = :idCuenta")
    fun obtenerCuentaConContactos(idCuenta: Int): List<CuentaConContactos>

    @Transaction
    @Query("SELECT * FROM cuentas WHERE link = :link")
    fun obtenerCuentaConContactosPorLink(link: String): List<CuentaConContactos>

    @Transaction
    suspend fun deleteCuentaAnotable(suceso: Int) {
        deleteById(suceso)
        deleteAnotable(suceso)
    }

    @Query("DELETE FROM cuentas WHERE idAnotable = :idAnotable")
    suspend fun deleteById(idAnotable: Int)

    @Query("DELETE FROM cuentas WHERE idAnotable = :idAnotable")
    suspend fun deleteAnotable(idAnotable: Int)

    @Insert
    suspend fun insertarRelaciones(relaciones: List<CuentaContactoCrossRef>)

    @Query("DELETE FROM cuenta_contacto_cross_ref WHERE idCuenta = :idAnotable")
    suspend fun eliminarRelacionesPorCuenta(idAnotable: Int)

    @Query("SELECT * FROM cuenta_contacto_cross_ref WHERE idContacto = :idAnotable")
    suspend fun findCuentasByContacto(idAnotable : Int) : List<CuentaContactoCrossRef>

    @Transaction
    @Query("SELECT * FROM cuenta_contacto_cross_ref WHERE idCuenta = :idAnotable")
    suspend fun findContactosByCuenta(idAnotable: Int): List<CuentaContactoCrossRef>

}