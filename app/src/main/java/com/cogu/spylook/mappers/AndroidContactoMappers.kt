package com.cogu.spylook.mappers

import com.cogu.domain.model.Contacto
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.cards.ContactoMiniCard

fun Contacto.toCardItem(): ContactoCardItem {
    return ContactoCardItem(
        idAnotable = idAnotable,
        nombre = nombre,
        alias = alias,
        colorFoto = colorFoto,
        clickable = true
    )
}

fun Contacto.toMiniCard(): ContactoMiniCard {
    return ContactoMiniCard(
        idAnotable = idAnotable,
        alias = alias,
        colorFoto = colorFoto
    )
}