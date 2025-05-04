package com.cogu.spylook.model.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(tableName = "contactos")
public class Contacto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String alias;
    private int edad;
    private int foto;
    private LocalDate fechaNacimiento;
    private String ciudad;
    private String estado;
    private String pais;

    public Contacto(int id, String nombre, String nickMasConocido, LocalDate fechaNacimiento, String ciudad, String estado, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.alias = nickMasConocido;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = LocalDate.now().isAfter(
                fechaNacimiento.withYear(LocalDate.now().getYear())
                        .withDayOfYear(LocalDate.now().getDayOfYear() - 1))
                ? LocalDate.now().getYear() - fechaNacimiento.getYear()
                : LocalDate.now().getYear() - (fechaNacimiento.getYear() - 1);
        this.ciudad = ciudad;
        this.estado = estado;
        this.pais = pais;
    }
    public Contacto(String nombre, String nickMasConocido, LocalDate fechaNacimiento, String ciudad, String estado, String pais) {
        this.nombre = nombre;
        this.alias = nickMasConocido;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = LocalDate.now().isAfter(
                fechaNacimiento.withYear(LocalDate.now().getYear())
                        .withDayOfYear(LocalDate.now().getDayOfYear() - 1))
                ? LocalDate.now().getYear() - fechaNacimiento.getYear()
                : LocalDate.now().getYear() - (fechaNacimiento.getYear() - 1);
        this.ciudad = ciudad;
        this.estado = estado;
        this.pais = pais;
    }
}
