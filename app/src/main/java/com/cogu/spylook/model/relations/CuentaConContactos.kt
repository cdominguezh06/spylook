package com.cogu.spylook.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class CuentaConContactos(
    @Embedded
    val cuenta: Cuenta,
    @Relation(
        parentColumn = "idCuenta",
        entityColumn = "idContacto",
        associateBy = Junction(
            value = CuentaContactoCrossRef::class,
            parentColumn = "idCuenta",
            entityColumn = "idContacto"
        )
    )
    val contactos: List<Contacto>
)