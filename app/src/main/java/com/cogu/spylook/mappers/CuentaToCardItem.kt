package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.CuentaCardItem
import com.cogu.spylook.model.entity.Cuenta
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface CuentaToCardItem {

    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(cuenta: Cuenta): CuentaCardItem
}