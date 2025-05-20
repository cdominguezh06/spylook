package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Grupo

class CreadorGrupo {
    @JvmField
    @Embedded
    var contacto: Contacto? = null

    @JvmField
    @Relation(parentColumn = "idAnotable", entityColumn = "idCreador")
    var grupos: MutableList<Grupo?>? = null
}
