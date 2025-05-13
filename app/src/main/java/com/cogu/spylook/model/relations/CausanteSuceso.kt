package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Suceso

class CausanteSuceso {
    @Embedded
    var contacto: Contacto? = null

    @Relation(parentColumn = "id", entityColumn = "idCausante")
    var sucesos: MutableList<Suceso?>? = null
}
