package com.cogu.spylook.mappers

import com.cogu.domain.model.Cuenta
import com.cogu.spylook.model.cards.CuentaCardItem

fun Cuenta.toCardItem(): CuentaCardItem {
    return CuentaCardItem(
        idAnotable = idAnotable,
        nombre = nombre,
        link = link,
        redSocial = redSocial,
        colorFoto = colorFoto,
        clickable = true
    )
}