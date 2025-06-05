package com.cogu.data.mappers

import com.cogu.spylook.model.cards.SucesoCardItem
import com.cogu.spylook.model.entity.SucesoEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface SucesoToCardItem {

    @Mapping(target = "clickable", constant = "true")
    @Mapping(target = "implicados", constant = " ")
    fun toCardItem(sucesoEntity: SucesoEntity): SucesoCardItem
}