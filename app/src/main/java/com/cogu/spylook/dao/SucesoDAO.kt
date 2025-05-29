package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.model.relations.ContactosSucesos
import com.cogu.spylook.model.relations.SucesosContactos

@Dao
interface SucesoDAO {

    @Insert
    suspend fun insert(suceso: Suceso)

    @Insert
    suspend fun addAnotable(anotable: Anotable): Long

    @Delete
    suspend fun deleteAnotable(anotable: Anotable)

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
    suspend fun deleteSucesoAnotable(suceso: Suceso) {
        deleteAnotable(suceso)
        delete(suceso)
    }

    @Delete
    suspend fun delete(suceso: Suceso)

    @Insert
    suspend fun insertarRelaciones(relaciones: List<ContactoSucesoCrossRef>)

    @Query("DELETE FROM contacto_suceso_cross_ref WHERE idSuceso = :idAnotable")
    suspend fun eliminarRelacionesPorSuceso(idAnotable: Int)

    @Query("SELECT * FROM contacto_suceso_cross_ref WHERE idContacto = :idAnotable")
    suspend fun findSucesosByImplicado(idAnotable : Int) : List<ContactoSucesoCrossRef>

    @Query("SELECT * FROM sucesos WHERE idCausante = :idAnotable")
    suspend fun findSucesosByCausante(idAnotable: Int): List<Suceso>
    @Transaction
    @Query("SELECT * FROM contacto_suceso_cross_ref WHERE idSuceso = :idAnotable")
    suspend fun getRelacionesBySuceso(idAnotable: Int): List<ContactoSucesoCrossRef>
}