package com.cogu.spylook.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cogu.spylook.model.entity.Suceso;
import com.cogu.spylook.model.relationships.ContactosSucesos;
import com.cogu.spylook.model.relationships.SucesosContactos;

import java.util.List;

@Dao
public interface SucesoDAO {
    @Insert
    void insert(Suceso suceso);

    @Update
    void update(Suceso suceso);

    @Delete
    void delete(Suceso suceso);

    @Transaction
    @Query("SELECT * FROM contactos")
    List<ContactosSucesos> getContactosWithSucesos();

    @Transaction
    @Query("SELECT * FROM sucesos")
    List<SucesosContactos> getSucesosWithContactos();
}
