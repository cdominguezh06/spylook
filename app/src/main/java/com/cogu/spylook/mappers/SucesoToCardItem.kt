package com.cogu.spylook.mappers

import com.cogu.domain.model.Suceso
import com.cogu.spylook.model.cards.SucesoCardItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface SucesoToCardItem {

    @Mapping(target = "clickable", constant = "true")
    @Mapping(target = "implicados", constant = " ")
    fun toCardItem(sucesoEntity: Suceso): SucesoCardItem
}