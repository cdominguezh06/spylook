package com.cogu.domain.repository

import com.cogu.domain.crossrefs.GrupoMiembro
import com.cogu.domain.model.Anotable
import com.cogu.domain.model.Grupo
import com.cogu.domain.relations.ContactosConGrupos
import com.cogu.domain.relations.GruposConContactos
import kotlinx.coroutines.flow.Flow

interface GrupoRepository {
    suspend fun addAnotable(anotable: Anotable): Long
    suspend fun deleteAnotable(idAnotable: Int)
    suspend fun updateAnotable(anotable: Anotable)
    suspend fun addGrupo(grupo: Grupo): Long
    suspend fun addGrupoWithAnotable(grupo: Grupo): Long
    suspend fun updateGrupo(grupo: Grupo)
    suspend fun updateGrupoWithAnotable(grupo: Grupo)
    suspend fun deleteGrupo(idAnotable: Int)
    suspend fun deleteGrupoWithAnotable(idAnotable: Int)
    fun findGrupoById(id: Int): Flow<Grupo>
    fun getGrupos(): Flow<List<Grupo>>

    fun getContactosWithGrupos(): Flow<List<ContactosConGrupos>>
    fun getGruposWithContactos(): Flow<List<GruposConContactos>>

    suspend fun insertarRelacion(idGrupo: Int, idContacto: Int)
    suspend fun eliminarRelacionesPorGrupo(idGrupo: Int)
    suspend fun eliminarMiembroDeGrupo(idGrupo: Int, idContacto: Int)
    fun findGruposByMiembro(idMiembro: Int): Flow<List<GrupoMiembro>>
    fun findGruposByCreador(idCreador: Int): Flow<List<Grupo>>
    fun getRelacionesByGrupo(idGrupo: Int): Flow<List<GrupoMiembro>>
}