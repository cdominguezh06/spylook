package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.SucesoCardItem
import com.cogu.spylook.model.entity.Suceso
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface SucesoToCardItem {

    @Mapping(target = "clickable", constant = "true")
    @Mapping(target = "implicados", constant = " ")
    fun toCardItem(suceso: Suceso): SucesoCardItem
}