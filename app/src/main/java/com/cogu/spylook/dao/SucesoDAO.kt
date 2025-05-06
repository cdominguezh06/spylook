package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.model.relationships.ContactosSucesos
import com.cogu.spylook.model.relationships.SucesosContactos

@Dao
interface SucesoDAO {

    @Insert
    suspend fun insert(suceso: Suceso)

    @Update
    suspend fun update(suceso: Suceso)

    @Delete
    suspend fun delete(suceso: Suceso)

    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getContactosWithSucesos(): List<ContactosSucesos>

    @Transaction
    @Query("SELECT * FROM sucesos")
    suspend fun getSucesosWithContactos(): List<SucesosContactos>
}