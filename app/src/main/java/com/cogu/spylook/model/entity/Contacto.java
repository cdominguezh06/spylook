package com.cogu.spylook.model.entity;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(tableName = "contactos")
public class Contacto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String alias;
    @Ignore
    private int edad;
    private int foto;
    private LocalDate fechaNacimiento;
    private String ciudad;
    private String estado;
    private String pais;

    public Contacto(int id, String nombre, String alias, LocalDate fechaNacimiento, String ciudad, String estado, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.alias = alias;
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
    @Ignore
    public Contacto(String nombre, String alias, LocalDate fechaNacimiento, String ciudad, String estado, String pais) {
        this.nombre = nombre;
        this.alias = alias;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
