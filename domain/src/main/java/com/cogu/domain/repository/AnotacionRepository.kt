package com.cogu.domain.repository

import com.cogu.domain.model.Anotacion
import kotlinx.coroutines.flow.Flow

interface AnotacionRepository {
    suspend fun addAnotacion(anotacion: Anotacion) : Long
    suspend fun deleteAnotacion(anotacion: Anotacion)
    fun findAnotacionById(id: Int): Flow<Anotacion>
    fun getAnotacionesDeAnotable(idAnotable: Int): Flow<List<Anotacion>>
    fun getAnotaciones(): Flow<List<Anotacion>>
}