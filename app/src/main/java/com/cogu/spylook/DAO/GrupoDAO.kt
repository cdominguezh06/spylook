package com.cogu.spylook.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cogu.spylook.model.entity.Grupo;
import com.cogu.spylook.model.relationships.ContactosGrupos;
import com.cogu.spylook.model.relationships.GruposContactos;

import java.util.List;

@Dao
public interface GrupoDAO {
    @Insert
    public void addGrupo(Grupo grupo);

    @Update
    public void updateGrupo(Grupo grupo);

    @Delete
    public void deleteGrupo(Grupo grupo);

    @Query("Select * from grupos where id = :id")
    public LiveData<Grupo> findGrupoById(int id);

    @Query("Select * from grupos")
    public LiveData<List<Grupo>> getGrupos();

    /**
     * Relacion entre grupos y contactos
     * @return Un objeto ContactoGrupos el cual cuenta con el contacto y la lista de grupos a la que pertenece
     */
    @Transaction
    @Query("SELECT * FROM contactos")
    List<ContactosGrupos> getContactosWithGrupos();

    /**
     * Relacion entre grupos y contactos
     * @return Un objeto GruposContactos el cual cuenta con el grupo y la lista de contactos miembros
     */
    @Transaction
    @Query("SELECT * FROM grupos")
    LiveData<List<GruposContactos>> getGruposWithContactos();
}
