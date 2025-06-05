package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.CuentaEntity
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class CuentaConContactos(
    @Embedded
    val cuentaEntity: CuentaEntity,
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = CuentaContactoCrossRef::class,
            parentColumn = "idCuenta",
            entityColumn = "idContacto"
        )
    )
    val contactoEntities: List<ContactoEntity>
)