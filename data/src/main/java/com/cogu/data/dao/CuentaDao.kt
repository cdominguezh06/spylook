package com.cogu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.data.crossrefs.CuentaContactoCrossRef
import com.cogu.data.entity.AnotableEntity
import com.cogu.data.entity.CuentaEntity
import com.cogu.data.relations.CuentaConContactos

@Dao
interface CuentaDao {

    @Insert
    suspend fun addAnotable(anotable: AnotableEntity): Long

    @Insert
    suspend fun insert(cuenta: CuentaEntity)

    @Query("SELECT * FROM cuentas WHERE idAnotable = :id")
    suspend fun findCuentaById(id: Int): CuentaEntity?

    @Query("SELECT * FROM cuentas WHERE idPropietario = :id")
    suspend fun findCuentasByPropietario(id: Int): List<CuentaEntity>

    @Transaction
    suspend fun addCuentaWithAnotable(cuenta: CuentaEntity) : Long {
        val idAnotable = addAnotable(
            AnotableEntity(
                idAnotable = cuenta.idAnotable,
                nombre = cuenta.nombre
            )
        )
        cuenta.idAnotable = idAnotable.toInt()
        insert(cuenta)
        return idAnotable
    }

    @Transaction
    suspend fun updateCuentaAnotable(cuenta: CuentaEntity) {
        updateAnotable(cuenta)
        update(cuenta)
    }

    @Update
    suspend fun updateAnotable(anotable: AnotableEntity)

    @Update
    suspend fun update(cuenta: CuentaEntity)

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

    @Delete
    suspend fun deleteMiembroDeCuenta(crossRef: CuentaContactoCrossRef)

    @Query("DELETE FROM cuenta_contacto_cross_ref WHERE idCuenta = :idAnotable")
    suspend fun eliminarRelacionesPorCuenta(idAnotable: Int)

    @Query("SELECT * FROM cuenta_contacto_cross_ref WHERE idContacto = :idAnotable")
    suspend fun findCuentasByContacto(idAnotable : Int) : List<CuentaContactoCrossRef>

    @Transaction
    @Query("SELECT * FROM cuenta_contacto_cross_ref WHERE idCuenta = :idAnotable")
    suspend fun findContactosByCuenta(idAnotable: Int): List<CuentaContactoCrossRef>

}