package com.cogu.spylook.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.relationships.CreadorGrupo;

import java.util.List;

@Dao
public interface ContactoDAO {

    @Insert
    public void addContacto(Contacto contacto);

    @Query("Select * from contactos where id = :id")
    public Contacto findContactoById(int id);

    @Query("Select * from contactos")
    public List<Contacto> getContactos();

    @Transaction
    @Query("SELECT * FROM contactos")
    List<CreadorGrupo> getCreadoresDeGrupos();
}
