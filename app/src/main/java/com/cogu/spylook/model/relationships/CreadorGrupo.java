package com.cogu.spylook.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.Grupo;

import java.util.List;

public class CreadorGrupo {
    @Embedded
    private Contacto contacto;

    @Relation(parentColumn = "id", entityColumn = "idCreador")
    private List<Grupo> grupos;

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
}
