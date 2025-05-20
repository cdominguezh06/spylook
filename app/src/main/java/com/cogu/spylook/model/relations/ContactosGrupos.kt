package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.Grupo


class ContactosGrupos {
    @JvmField
    @Embedded
    var contacto: Contacto? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = ContactoGrupoCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idGrupo"
        )
    )
    var grupos: MutableList<Grupo?>? = null
}
