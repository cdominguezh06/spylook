package com.cogu.spylook.model.entity;

import androidx.room.Entity;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(tableName = "sucesos")
@NoArgsConstructor
public class Suceso {
    private int id;
    private LocalDate fecha;
    private String descripcion;
    private int idCausante;
}
