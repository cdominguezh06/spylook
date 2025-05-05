package com.cogu.spylook.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.relationships.ContactoAnotacion

@Dao
interface AnotacionDAO {

    @Insert
    suspend fun addAnotacion(anotacion: Anotacion)

    @Update
    fun updateAnotacion(anotacion: Anotacion)

    @Delete
    fun deleteAnotacion(anotacion: Anotacion)

    @Query("SELECT * FROM anotaciones WHERE id = :id")
    fun findAnotacionById(id: Int): Anotacion

    @Transaction
    @Query("SELECT * FROM contactos")
    fun getContactosWithAnotaciones():List<ContactoAnotacion>
}