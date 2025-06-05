package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.data.crossrefs.ContactoGrupoCrossRef
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.GrupoEntity

class GruposContactos {
    @JvmField
    @Embedded
    var grupoEntity: GrupoEntity? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = ContactoGrupoCrossRef::class,
            parentColumn = "idGrupo",
            entityColumn = "idContacto"
        )
    )
    var contactoEntities: MutableList<ContactoEntity?>? = null
}
