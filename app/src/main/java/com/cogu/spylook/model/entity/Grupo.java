package com.cogu.spylook.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(tableName = "grupos")
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    public int id_creador;
}
