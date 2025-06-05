package com.cogu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.model.relations.ContactosGrupos
import com.cogu.spylook.model.relations.GruposContactos

@Dao
interface GrupoDAO {
    @Insert
    suspend fun addAnotable(anotable: Anotable): Long

    @Insert
    suspend fun addGrupo(grupo: Grupo) : Long

    @Query("DELETE FROM anotables WHERE idAnotable = :idAnotable")
    suspend fun deleteAnotable(idAnotable: Int)

    @Transaction
    suspend fun addGrupoWithAnotable(grupo: Grupo) : Long {
        val idAnotable = addAnotable(
            Anotable(
                idAnotable = grupo.idAnotable,
                nombre = grupo.nombre
            )
        )
        grupo.idAnotable = idAnotable.toInt()
       return addGrupo(grupo)
    }

    @Transaction
    suspend fun updateGrupoAnotable(grupo: Grupo) {
        updateAnotable(grupo)
        updateGrupo(grupo)
    }

    @Update
    suspend fun updateAnotable(anotable: Anotable)
    @Update
    suspend fun updateGrupo(grupo: Grupo)

    @Transaction
    suspend fun deleteGrupoAnotable(idAnotable: Int) {
        delete(idAnotable)
        deleteAnotable(idAnotable)
    }

    @Query("DELETE FROM grupos WHERE idAnotable = :idAnotable")
    suspend fun delete(idAnotable: Int)

    @Query("SELECT * FROM grupos WHERE idAnotable = :id")
    suspend fun findGrupoById(id: Int): Grupo?

    @Query("SELECT * FROM grupos")
    suspend fun getGrupos(): List<Grupo>

    /**
     * Relación entre grupos y contactos
     * @return Un objeto ContactoGrupos el cual cuenta con el contacto y la lista de grupos a la que pertenece
     */
    @Transaction
    @Query("SELECT * FROM contactos")
    suspend fun getContactosWithGrupos(): List<ContactosGrupos>

    /**
     * Relación entre grupos y contactos
     * @return Un objeto GruposContactos el cual cuenta con el grupo y la lista de contactos miembros
     */
    @Transaction
    @Query("SELECT * FROM grupos")
    suspend fun getGruposWithContactos(): List<GruposContactos>

    @Insert
    suspend fun insertarRelaciones(relaciones: List<ContactoGrupoCrossRef>)

    @Query("DELETE FROM contacto_grupo_cross_ref WHERE idGrupo = :idGrupo")
    suspend fun eliminarRelacionesPorGrupo(idGrupo: Int)

    @Delete
    suspend fun deleteMiembroDeGrupo(crossRef: ContactoGrupoCrossRef)

    @Query("SELECT * FROM contacto_grupo_cross_ref WHERE idContacto = :idMiembro")
    suspend fun findGruposByMiembro(idMiembro : Int) : List<ContactoGrupoCrossRef>

    @Query("SELECT * FROM grupos WHERE idCreador = :idAnotable")
    suspend fun findGruposByCreador(idAnotable: Int): List<Grupo>
    @Transaction
    @Query("SELECT * FROM contacto_grupo_cross_ref WHERE idGrupo = :idGrupo")
    suspend fun getRelacionesByGrupo(idGrupo: Int): List<ContactoGrupoCrossRef>

}
