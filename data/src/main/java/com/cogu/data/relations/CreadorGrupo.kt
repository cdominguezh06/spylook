package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.GrupoEntity

class CreadorGrupo {
    @JvmField
    @Embedded
    var contactoEntity: ContactoEntity? = null

    @JvmField
    @Relation(parentColumn = "idAnotable", entityColumn = "idCreador")
    var grupoEntities: MutableList<GrupoEntity?>? = null
}
