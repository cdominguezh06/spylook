package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.SucesoEntity

class ContactosSucesos {
    @JvmField
    @Embedded
    var contactoEntity: ContactoEntity? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = ContactoSucesoCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idSuceso"
        )
    )
    var sucesoEntities: MutableList<SucesoEntity?>? = null
}