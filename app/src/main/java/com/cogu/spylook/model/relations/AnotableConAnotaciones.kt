package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.entity.Anotacion

class AnotableConAnotaciones {
    @JvmField
    @Embedded
    var anotable: Anotable? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable"
    )
    var anotaciones: MutableList<Anotacion?>? = null
}
