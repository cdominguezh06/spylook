package com.cogu.data.mappers

import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.AnotacionEntity
import org.mapstruct.Mapper

@Mapper
interface AnotacionToCardItem {
    fun toCardItem(anotacionEntity: AnotacionEntity): AnotacionCardItem
    fun toAnotacion(anotacionCardItem: AnotacionCardItem): AnotacionEntity
}