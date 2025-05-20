package com.cogu.spylook.dao

import androidx.room.*
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.relations.AmigosDeContacto
import com.cogu.spylook.model.relations.ContactoConCuentas
import com.cogu.spylook.model.relations.CreadorGrupo

@Dao
interface ContactoDAO {

    @Insert
    suspend fun addContacto(contacto: Contacto)

    @Update
    suspend fun updateContacto(contacto: Contacto)

    @Delete
    suspend fun deleteContacto(contacto: Contacto)

    @Query("SELECT * FROM contactos WHERE idAnotable = :id")
    suspend fun findContactoById(id: Int): Contacto

    @Query("SELECT * FROM contactos")
    suspend fun getContactos(): List<Contacto>

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getCreadoresDeGrupos(): List<CreadorGrupo>

    @Insert
    suspend fun insertAmistad(crossRef: ContactoAmistadCrossRef)

    @Transaction
    @Query("SELECT * FROM contactos WHERE idAnotable = :id")
    suspend fun getAmigosDeContacto(id: Int): AmigosDeContacto

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getTodosLosContactosConAmigos(): List<AmigosDeContacto>

    @Transaction
    @Query("SELECT * FROM contactos WHERE idAnotable = :idContacto")
    fun obtenerContactoConCuentas(idContacto: Int): List<ContactoConCuentas>

}