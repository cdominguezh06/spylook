package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.SucesoEntity

class CausanteSuceso {
    @Embedded
    var contactoEntity: ContactoEntity? = null

    @Relation(parentColumn = "idAnotable", entityColumn = "idCausante")
    var sucesoEntities: MutableList<SucesoEntity?>? = null
}
