package com.cogu.spylook.model.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.Grupo

class GruposContactos {
    @JvmField
    @Embedded
    var grupo: Grupo? = null

    @JvmField
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ContactoGrupoCrossRef::class,
            parentColumn = "idGrupo",
            entityColumn = "idContacto"
        )
    )
    var contactos: MutableList<Contacto?>? = null
}
