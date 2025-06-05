package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.SucesoEntity

class SucesosContactos {
    @JvmField
    @Embedded
    var sucesoEntity: SucesoEntity? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = ContactoSucesoCrossRef::class,
            parentColumn = "idSuceso",
            entityColumn = "idContacto"
        )
    )
    var contactoEntities: MutableList<ContactoEntity?>? = null
}
