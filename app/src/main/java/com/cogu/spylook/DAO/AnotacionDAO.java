package com.cogu.spylook.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cogu.spylook.model.entity.Anotacion;
import com.cogu.spylook.model.relationships.ContactoAnotacion;

import java.util.List;

@Dao
public interface AnotacionDAO {
    @Insert
    public void addAnotacion(Anotacion anotacion);

    @Update
    public void updateAnotacion(Anotacion anotacion);

    @Delete
    public void deleteAnotacion(Anotacion anotacion);

    @Query("Select * from anotaciones where id = :id")
    public Anotacion findAnotacionById(int id);

    @Transaction
    @Query("SELECT * FROM contactos")
    List<ContactoAnotacion> getContactosWithAnotaciones();

}
