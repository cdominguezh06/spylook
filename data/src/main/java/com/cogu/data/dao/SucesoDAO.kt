package com.cogu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Suceso

@Dao
interface SucesoDAO {

    @Insert
    suspend fun insert(suceso: Suceso)

    @Insert
    suspend fun addAnotable(anotable: Anotable): Long

    @Query("DELETE FROM anotables WHERE idAnotable = :idAnotable")
    suspend fun deleteAnotable(idAnotable: Int)

    @Query("SELECT * FROM sucesos WHERE idAnotable = :id")
    suspend fun findSucesoById(id: Int): Suceso?

    @Update
    suspend fun update(suceso: Suceso)

    @Transaction
    suspend fun addSucesoAnotable(suceso: Suceso) : Long {
        val idAnotable = addAnotable(
            Anotable(
                idAnotable = suceso.idAnotable,
                nombre = suceso.nombre
            )
        )
        suceso.idAnotable = idAnotable.toInt()
        insert(suceso)
        return idAnotable
    }

    @Transaction
    suspend fun updateSucesoAnotable(suceso: Suceso) {
        updateAnotable(suceso)
        update(suceso)
    }

    @Update
    suspend fun updateAnotable(anotable: Anotable)

    @Transaction
    suspend fun deleteSucesoAnotable(idAnotable: Int) {
        delete(idAnotable)
        deleteAnotable(idAnotable)
    }

    @Query("DELETE FROM sucesos WHERE idAnotable = :idAnotable")
    suspend fun delete(idAnotable: Int)

    @Insert
    suspend fun insertarRelaciones(relaciones: List<ContactoSucesoCrossRef>)

    @Query("DELETE FROM contacto_suceso_cross_ref WHERE idSuceso = :idAnotable")
    suspend fun eliminarRelacionesPorSuceso(idAnotable: Int)


    @Delete
    suspend fun deleteImplicadoSuceso(crossRef: ContactoSucesoCrossRef)

    @Query("SELECT * FROM contacto_suceso_cross_ref WHERE idContacto = :idAnotable")
    suspend fun findSucesosByImplicado(idAnotable : Int) : List<ContactoSucesoCrossRef>

    @Query("SELECT * FROM sucesos WHERE idCausante = :idAnotable")
    suspend fun findSucesosByCausante(idAnotable: Int): List<Suceso>
    @Transaction
    @Query("SELECT * FROM contacto_suceso_cross_ref WHERE idSuceso = :idAnotable")
    suspend fun getRelacionesBySuceso(idAnotable: Int): List<ContactoSucesoCrossRef>
}