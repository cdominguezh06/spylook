package com.cogu.domain.repository

import com.cogu.domain.crossrefs.CuentaContacto
import com.cogu.domain.model.Anotable
import com.cogu.domain.model.Cuenta
import com.cogu.domain.relations.CuentasConContactos
import kotlinx.coroutines.flow.Flow

interface CuentaRepository {
    suspend fun addAnotable(anotable: Anotable): Long
    suspend fun updateAnotable(anotable: Anotable)
    suspend fun deleteAnotable(idAnotable: Int)

    suspend fun insertCuenta(cuenta: Cuenta)
    fun findCuentaById(id: Int): Flow<Cuenta>
    fun findCuentasByPropietario(idPropietario: Int): Flow<List<Cuenta>>
    suspend fun addCuentaWithAnotable(cuenta: Cuenta): Long
    suspend fun updateCuentaWithAnotable(cuenta: Cuenta)
    suspend fun deleteCuentaById(idAnotable: Int)
    fun obtenerCuentaConContactos(idCuenta: Int): Flow<List<CuentasConContactos>>
    fun obtenerCuentaConContactosPorLink(link: String): Flow<List<CuentasConContactos>>

    suspend fun insertarRelacion(idCuenta: Int, idContacto: Int)
    suspend fun eliminarMiembroDeCuenta(idCuenta: Int, idContacto: Int)
    suspend fun eliminarRelacionesPorCuenta(idCuenta: Int)
    fun findCuentasByContacto(idContacto: Int): Flow<List<CuentaContacto>>
    fun findContactosByCuenta(idCuenta: Int): Flow<List<CuentaContacto>>
}