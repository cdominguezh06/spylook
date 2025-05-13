package com.cogu.spylook.model.relations

import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class ContactoConCuentas(
    val contacto: Contacto,
    @Relation(
        parentColumn = "id",
        entityColumn = "cuentaId",
        associateBy = Junction(CuentaContactoCrossRef::class)
    )
    val cuentas: List<Cuenta>
)