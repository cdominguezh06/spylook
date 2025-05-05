package com.cogu.spylook.model.cards

import lombok.Data

@Data
class ContactoCardItem {
     var id = 0
     val nombre: String?
     val alias: String?
     val foto: Int
     var clickable = true

    constructor(id: Int, nombre: String?, alias: String?, foto: Int) {
        this.id = id
        this.nombre = nombre
        this.alias = alias
        this.foto = foto
    }

    constructor(nombre: String?, alias: String?, foto: Int, clickable: Boolean) {
        this.nombre = nombre
        this.alias = alias
        this.foto = foto
        this.clickable = clickable
    }
}
