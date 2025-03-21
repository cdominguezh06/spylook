package com.cogu.spylook.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Grupo {
    private int id;
    private String nombre;
    private int grupoImg;
    private Contacto creador;
    private List<Contacto> miembros;

    public Grupo(int id, String nombre, int grupoImg, Contacto creador) {
        this.id = id;
        this.nombre = nombre;
        this.grupoImg = grupoImg;
        this.creador = creador;
        this.miembros = new ArrayList<>();
    }
}
