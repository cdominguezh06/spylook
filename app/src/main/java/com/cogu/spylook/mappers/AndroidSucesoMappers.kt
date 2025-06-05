package com.cogu.spylook.mappers

import com.cogu.domain.model.Suceso
import com.cogu.spylook.model.cards.SucesoCardItem

fun Suceso.toCardItem(): SucesoCardItem {
    return SucesoCardItem(
        idAnotable = idAnotable,
        nombre = nombre,
        fecha = fecha,
        implicados = "",
        colorFoto = colorFoto,
        clickable = true
    )
}