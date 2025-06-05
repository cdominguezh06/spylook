package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.data.entity.AnotableEntity
import com.cogu.data.entity.AnotacionEntity

class AnotableConAnotaciones {
    @JvmField
    @Embedded
    var anotableEntity: AnotableEntity? = null

    @JvmField
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable"
    )
    var anotaciones: MutableList<AnotacionEntity?>? = null
}
