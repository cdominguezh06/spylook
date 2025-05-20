package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef

class AmigosDeContacto {
    @JvmField
    @Embedded
    var contacto: Contacto? = null

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
    var amigos: MutableList<Contacto?>? = null
}
