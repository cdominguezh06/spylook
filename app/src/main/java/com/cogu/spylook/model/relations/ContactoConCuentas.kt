package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class ContactoConCuentas(
    @Embedded
    val contacto: Contacto,
    @Relation(
        parentColumn = "idAnotable",
        entityColumn = "idAnotable",
        associateBy = Junction(
            value = CuentaContactoCrossRef::class,
            parentColumn = "idContacto",
            entityColumn = "idCuenta"
        )
    )
    val cuentas: List<Cuenta>
)