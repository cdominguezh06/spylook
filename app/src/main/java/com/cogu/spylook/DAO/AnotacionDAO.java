package com.cogu.spylook.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cogu.spylook.model.entity.Anotacion;

import java.util.List;

@Dao
public interface AnotacionDAO {
    @Insert
    public void addAnotacion(Anotacion anotacion);

    @Query("Select * from anotaciones where id = :id")
    public Anotacion findAnotacionById(int id);

    @Query("Select * from anotaciones where contacto_id = :id")
    public Anotacion findAnotacionByContactoId(int id);

    @Query("Select * from anotaciones")
    public List<Anotacion> findAllAnotaciones();

}
