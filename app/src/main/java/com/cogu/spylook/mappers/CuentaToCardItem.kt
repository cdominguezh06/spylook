package com.cogu.spylook.mappers

import com.cogu.domain.model.Cuenta
import com.cogu.spylook.model.cards.CuentaCardItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface CuentaToCardItem {

    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(cuenta: Cuenta): CuentaCardItem
}