package com.cogu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cogu.data.entity.AnotacionEntity

@Dao
interface AnotacionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnotacion(anotacion: AnotacionEntity) : Long

    @Delete
    suspend fun deleteAnotacion(anotacion: AnotacionEntity)

    @Query("SELECT * FROM anotaciones WHERE id = :id")
    suspend fun findAnotacionById(id: Int): AnotacionEntity?

    @Query("SELECT * FROM anotaciones WHERE idAnotable = :idAnotable")
    suspend fun getAnotacionesDeAnotable(idAnotable: Int): MutableList<AnotacionEntity>

}