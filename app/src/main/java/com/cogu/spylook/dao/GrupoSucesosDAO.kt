package com.cogu.spylook.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cogu.spylook.model.entity.Suceso

@Dao
interface GrupoSucesosDAO {
    // Obtener sucesos de los integrantes de un grupo y su fundador
    @Transaction
    @Query("""
        SELECT DISTINCT s.*
        FROM sucesos AS s
        LEFT JOIN contacto_suceso_cross_ref AS csc ON csc.idSuceso = s.idSuceso
        WHERE csc.idContacto IN (
            SELECT c.idAnotable
            FROM contactos AS c
            WHERE c.idAnotable IN (
                -- Obtener los contactos que son integrantes del grupo
                SELECT cg.idContacto
                FROM contacto_grupo_cross_ref AS cg
                WHERE cg.idGrupo = :idGrupo
            )
            OR c.idAnotable = (
                -- Considerar al fundador del grupo como contacto relacionado
                SELECT g.idCreador
                FROM grupos AS g
                WHERE g.idAnotable = :idGrupo
            )
        )
    """)
    suspend fun getSucesosFromGrupo(idGrupo: Int): List<Suceso>

}