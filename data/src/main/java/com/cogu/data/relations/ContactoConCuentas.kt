package com.cogu.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.data.crossrefs.CuentaContactoCrossRef
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.CuentaEntity

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