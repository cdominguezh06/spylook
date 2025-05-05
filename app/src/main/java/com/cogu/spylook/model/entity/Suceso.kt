package com.cogu.spylook.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "sucesos")
@NoArgsConstructor
public class Suceso {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDate fecha;
    private String descripcion;
    private int idCausante;

    public Suceso(LocalDate fecha, String descripcion, int idCausante) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.idCausante = idCausante;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCausante() {
        return idCausante;
    }

    public void setIdCausante(int idCausante) {
        this.idCausante = idCausante;
    }
}
