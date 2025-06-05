package com.cogu.spylook.mappers

import com.cogu.domain.model.Grupo
import com.cogu.spylook.model.cards.GrupoCardItem

fun Grupo.toCardItem(): GrupoCardItem {
    return GrupoCardItem(
        idAnotable = idAnotable,
        nombre = nombre,
        colorFoto = colorFoto,
        miembros = 0,
        clickable = true
    )
}