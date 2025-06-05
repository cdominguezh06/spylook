package com.cogu.data.dao

import androidx.room.*
import com.cogu.data.crossrefs.ContactoAmistadCrossRef
import com.cogu.data.entity.AnotableEntity
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.relations.ContactoConCuentas
import com.cogu.data.relations.CreadorGrupo

@Dao
interface ContactoDAO {

    @Insert
    suspend fun addAnotable(anotable: AnotableEntity): Long

    @Update
    suspend fun updateAnotable(anotable: AnotableEntity)

    @Insert
    suspend fun addContacto(contacto: ContactoEntity)

    @Transaction
    suspend fun addContactoWithAnotable(contacto: ContactoEntity) {
        val idAnotable = addAnotable(
            AnotableEntity(
                idAnotable = contacto.idAnotable,
                nombre = contacto.nombre
            )
        )
        contacto.idAnotable = idAnotable.toInt()
        addContacto(contacto)
    }


    @Transaction
    suspend fun updateContactoWithAnotable(contacto: ContactoEntity) {
        updateAnotable(
            AnotableEntity(
                idAnotable = contacto.idAnotable,
                nombre = contacto.nombre
            )
        )
        updateContacto(contacto)
    }

    @Update
    suspend fun updateContacto(contacto: ContactoEntity)

    @Delete
    suspend fun deleteContacto(contacto: ContactoEntity)

    @Query("DELETE FROM contactos WHERE idAnotable = :id")
    suspend fun deleteContactosById(id: Int)

    @Query("DELETE FROM anotables WHERE idAnotable = :id")
    suspend fun deleteAnotableById(id: Int)

    @Transaction
    suspend fun deleteContactoWithAnotableById(id : Int) {
        deleteContactosById(id)
        deleteAnotableById(id)
    }

    @Query("SELECT * FROM contactos WHERE idAnotable = :id")
    suspend fun findContactoById(id: Int): ContactoEntity

    @Query("SELECT * FROM anotables WHERE idAnotable = :id")
    suspend fun findAnotableById(id: Int): AnotableEntity

    @Query("SELECT * FROM contactos WHERE nombre = :nombre")
    suspend fun findContactoByName(nombre: String): ContactoEntity?

    @Query("SELECT * FROM contactos")
    suspend fun getContactos(): List<ContactoEntity>

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getCreadoresDeGrupos(): List<CreadorGrupo>

    @Insert
    suspend fun insertAmistad(crossRef: ContactoAmistadCrossRef)

    @Delete
    suspend fun deleteAmistad(crossRef: ContactoAmistadCrossRef)

    @Transaction
    @Query("SELECT * FROM contacto_amistad_cross_ref WHERE idContacto = :idContacto")
    suspend fun getAmistadesPorContacto(idContacto: Int): List<ContactoAmistadCrossRef>

    @Transaction
    @Query("SELECT * FROM contacto_amistad_cross_ref WHERE idAmigo = :idAmigo")
    suspend fun getContactosPorAmigo(idAmigo: Int): List<ContactoAmistadCrossRef>
    @Transaction
    @Query("SELECT * FROM contactos WHERE idAnotable = :idContacto")
    fun obtenerContactoConCuentas(idContacto: Int): List<ContactoConCuentas>

}