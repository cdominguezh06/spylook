package com.cogu.spylook.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "anotaciones")
public class Anotacion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDate fecha;
    private String titulo;
    private String descripcion;
    private int idContacto;

}
