package com.cogu.spylook.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef;
import com.cogu.spylook.model.relationships.AmigosDeContacto;
import com.cogu.spylook.model.relationships.CreadorGrupo;

import java.util.List;

@Dao
public interface ContactoDAO {

    @Insert
    public void addContacto(Contacto contacto);

    @Update
    public void updateContacto(Contacto contacto);

    @Delete
    public void deleteContacto(Contacto contacto);

    @Query("Select * from contactos where id = :id")
    public Contacto findContactoById(int id);

    @Query("Select * from contactos")
    public List<Contacto> getContactos();

    @Transaction
    @Query("SELECT * FROM contactos")
    List<CreadorGrupo> getCreadoresDeGrupos();

    @Insert
    void insertAmistad(ContactoAmistadCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM contactos WHERE id = :id")
    AmigosDeContacto getAmigosDeContacto(int id);

    @Transaction
    @Query("SELECT * FROM contactos")
    List<AmigosDeContacto> getTodosLosContactosConAmigos();
}
