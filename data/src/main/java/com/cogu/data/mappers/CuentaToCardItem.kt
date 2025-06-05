package com.cogu.data.mappers

import com.cogu.spylook.model.cards.CuentaCardItem
import com.cogu.spylook.model.entity.CuentaEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface CuentaToCardItem {

    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(cuentaEntity: CuentaEntity): CuentaCardItem
}