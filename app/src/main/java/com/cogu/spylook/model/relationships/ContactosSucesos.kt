package com.cogu.spylook.model.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Suceso

class ContactosSucesos {
    @JvmField
    @Embedded
    var contacto: Contacto? = null

    @JvmField
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ContactoSucesoCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idSuceso"
        )
    )
    var sucesos: MutableList<Suceso?>? = null
}