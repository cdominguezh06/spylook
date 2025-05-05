package com.cogu.spylook.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Contacto

class ContactoAnotacion {
    @JvmField
    @Embedded
    var contacto: Contacto? = null

    @JvmField
    @Relation(parentColumn = "id", entityColumn = "idContacto")
    var anotaciones: MutableList<Anotacion?>? = null
}
