package com.cogu.spylook.dao

import androidx.room.*
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.relations.AmigosDeContacto
import com.cogu.spylook.model.relations.ContactoConCuentas
import com.cogu.spylook.model.relations.CreadorGrupo

@Dao
interface ContactoDAO {

    @Insert
    suspend fun addAnotable(anotable: Anotable): Long

    @Insert
    suspend fun addContacto(contacto: Contacto)

    @Transaction
    suspend fun addContactoWithAnotable(contacto: Contacto) {
        val idAnotable = addAnotable(
            Anotable(
                idAnotable = contacto.idAnotable,
                nombre = contacto.nombre
            )
        )
        contacto.idAnotable = idAnotable.toInt()
        addContacto(contacto)
    }

    @Update
    suspend fun updateContacto(contacto: Contacto)

    @Delete
    suspend fun deleteContacto(contacto: Contacto)

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
    suspend fun findContactoById(id: Int): Contacto

    @Query("SELECT * FROM contactos WHERE nombre = :nombre")
    suspend fun findContactoByName(nombre: String): Contacto?

    @Query("SELECT * FROM contactos")
    suspend fun getContactos(): List<Contacto>

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getCreadoresDeGrupos(): List<CreadorGrupo>

    @Insert
    suspend fun insertAmistad(crossRef: ContactoAmistadCrossRef)

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