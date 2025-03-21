package com.cogu.spylook.model;


import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contacto {
    private int id;
    private String nombre;
    private String nickMasConocido;
    private int foto;
    private int edad;
    private LocalDate fechaNacimiento;
    private String ciudad;
    private String estado;
    private String pais;

    public Contacto(int id, String nombre, String nickMasConocido, int foto, int edad, LocalDate fechaNacimiento, String ciudad, String estado, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.nickMasConocido = nickMasConocido;
        this.foto = foto;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.ciudad = ciudad;
        this.estado = estado;
        this.pais = pais;
    }
}
