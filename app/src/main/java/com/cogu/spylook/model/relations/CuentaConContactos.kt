package com.cogu.spylook.model.relations

import androidx.room.Junction
import androidx.room.Relation
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef

data class CuentaConContactos(
    val cuenta: Cuenta,
    @Relation(
        parentColumn = "id",
        entityColumn = "contactoId",
        associateBy = Junction(CuentaContactoCrossRef::class)
    )
    val contactos: List<Contacto>
)