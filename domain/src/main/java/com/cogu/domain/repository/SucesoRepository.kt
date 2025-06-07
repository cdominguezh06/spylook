package com.cogu.domain.repository

import com.cogu.domain.crossrefs.ContactoSuceso
import com.cogu.domain.model.Anotable
import com.cogu.domain.model.Suceso
import kotlinx.coroutines.flow.Flow

interface SucesoRepository {
    suspend fun addAnotable(anotable: Anotable): Long
    suspend fun deleteAnotable(idAnotable: Int)
    suspend fun updateAnotable(anotable: Anotable)

    suspend fun insertSuceso(suceso: Suceso)
    suspend fun addSucesoWithAnotable(suceso: Suceso): Long
    suspend fun updateSuceso(suceso: Suceso)
    suspend fun updateSucesoWithAnotable(suceso: Suceso)
    suspend fun deleteSuceso(idAnotable: Int)
    suspend fun deleteSucesoWithAnotable(idAnotable: Int)
    fun findSucesoById(id: Int): Flow<Suceso?>
    fun findSucesosByCausante(idCausante: Int): Flow<List<Suceso>>

    suspend fun insertarRelaciones(relaciones: List<ContactoSuceso>)
    suspend fun eliminarRelacionesPorSuceso(idSuceso: Int)
    suspend fun eliminarImplicadoDeSuceso(ref : ContactoSuceso)
    fun findSucesosByImplicado(idContacto: Int): Flow<List<ContactoSuceso>>
    fun getRelacionesBySuceso(idSuceso: Int): Flow<List<ContactoSuceso>>
}