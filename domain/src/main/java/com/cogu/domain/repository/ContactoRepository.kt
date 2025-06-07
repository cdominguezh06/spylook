package com.cogu.domain.repository

import com.cogu.domain.crossrefs.ContactoAmistad
import com.cogu.domain.model.Anotable
import com.cogu.domain.model.Contacto
import com.cogu.domain.relations.ContactoConCuentas
import com.cogu.domain.relations.CreadorConGrupos

import kotlinx.coroutines.flow.Flow

interface ContactoRepository {
    suspend fun addAnotable(): Long
    suspend fun deleteAnotable(anotable: Anotable)
    suspend fun updateAnotable(anotable: Anotable)
    suspend fun addContactoWithAnotable(contacto: Contacto)
    suspend fun deleteContacto(contacto: Contacto)
    suspend fun updateContacto(contacto: Contacto)
    fun getContactoById(id: Int): Flow<Contacto>
    suspend fun deleteContactoById(id: Int)
    suspend fun deleteAnotableById(id: Int)
    fun getContactos(): Flow<List<Contacto>>
    fun getCreadoresDeGrupos(): Flow<List<CreadorConGrupos>>
    suspend fun insertAmistad(idContacto: Int, idAmigo: Int)
    suspend fun deleteAmistad(idContacto: Int, idAmigo: Int)
    fun getAmistadesPorContacto(idContacto: Int): Flow<List<ContactoAmistad>>
    fun getContactosPorAmigo(idAmigo: Int): Flow<List<ContactoAmistad>>
    fun obtenerContactoConCuentas(idContacto: Int): Flow<List<ContactoConCuentas>>
}