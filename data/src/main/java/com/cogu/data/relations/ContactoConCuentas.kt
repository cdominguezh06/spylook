package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.CuentaEntity
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class ContactoConCuentas(
    @Embedded
    val contactoEntity: ContactoEntity,
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = CuentaContactoCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idCuenta"
        )
    )
    val cuentaEntities: List<CuentaEntity>
)