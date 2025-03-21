package com.cogu.spylook.model.cards;

import lombok.Data;

@Data
public class ContactoCardItem {
    private int id;
    private String nombre;
    private String nickMasConocido;
    private int foto;
    private boolean clickable = true;

    public ContactoCardItem(int id, String nombre, String nickMasConocido, int foto) {
        this.id = id;
        this.nombre = nombre;
        this.nickMasConocido = nickMasConocido;
        this.foto = foto;
    }

    public ContactoCardItem(String nombre, String nickMasConocido, int foto, boolean clickable) {
        this.nombre = nombre;
        this.nickMasConocido = nickMasConocido;
        this.foto = foto;
        this.clickable = clickable;
    }
}
