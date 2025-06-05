package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.SucesoEntity

class CausanteSuceso {
    @Embedded
    var contactoEntity: ContactoEntity? = null

    @Relation(parentColumn = "idAnotable", entityColumn = "idCausante")
    var sucesoEntities: MutableList<SucesoEntity?>? = null
}
