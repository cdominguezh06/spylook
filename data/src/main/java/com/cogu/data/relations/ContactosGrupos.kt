package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.GrupoEntity


class ContactosGrupos {
    @JvmField
    @Embedded
    var contactoEntity: ContactoEntity? = null

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
    var grupoEntities: MutableList<GrupoEntity?>? = null
}
