package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Suceso

class SucesosContactos {
    @JvmField
    @Embedded
    var suceso: Suceso? = null

    @JvmField
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ContactoSucesoCrossRef::class,
            parentColumn = "idSuceso",
            entityColumn = "idContacto"
        )
    )
    var contactos: MutableList<Contacto?>? = null
}
