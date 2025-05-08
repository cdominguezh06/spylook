package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.relationships.ContactoAnotacion

@Dao
interface AnotacionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnotacion(anotacion: Anotacion)

    @Delete
    suspend fun deleteAnotacion(anotacion: Anotacion)

    @Query("SELECT * FROM anotaciones WHERE id = :id")
    suspend fun findAnotacionById(id: Int): Anotacion

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getContactosWithAnotaciones():List<ContactoAnotacion>

    @Query("SELECT * FROM anotaciones WHERE idContacto = :idContacto")
    suspend fun getAnotacionesContacto(idContacto: Int): MutableList<Anotacion>

}