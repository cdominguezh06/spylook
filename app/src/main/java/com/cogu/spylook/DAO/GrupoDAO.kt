package com.cogu.spylook.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.relationships.ContactosGrupos
import com.cogu.spylook.model.relationships.GruposContactos

@Dao
interface GrupoDAO {

    @Insert
    suspend fun addGrupo(grupo: Grupo)

    @Update
    suspend fun updateGrupo(grupo: Grupo)

    @Delete
    suspend fun deleteGrupo(grupo: Grupo)

    @Query("SELECT * FROM grupos WHERE id = :id")
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
}