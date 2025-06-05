package com.cogu.spylook.mappers

import com.cogu.domain.model.Anotacion
import com.cogu.spylook.model.cards.AnotacionCardItem

fun Anotacion.toCardItem(): AnotacionCardItem {
    return AnotacionCardItem(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        fecha = fecha,
        idAnotable = idAnotable
    )
}

fun AnotacionCardItem.toModel(): Anotacion {
    return Anotacion(
        id = id,
        fecha = fecha,
        titulo = titulo,
        descripcion = descripcion,
        idAnotable = idAnotable
    )
}