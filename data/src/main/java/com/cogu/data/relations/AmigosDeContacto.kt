package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.data.crossrefs.ContactoAmistadCrossRef
import com.cogu.data.entity.ContactoEntity

class AmigosDeContacto {
    @JvmField
    @Embedded
    var contactoEntity: ContactoEntity? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = ContactoAmistadCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idAmigo"
        )
    )
    var amigos: MutableList<ContactoEntity?>? = null
}
