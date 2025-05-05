package com.cogu.spylook.model.entity

import androidx.room.Entity

@Entity(primaryKeys = ["idContacto", "idGrupo"])
class ContactoGrupoCrossRef {
    var idContacto: Int = 0
    var idGrupo: Int = 0
}
