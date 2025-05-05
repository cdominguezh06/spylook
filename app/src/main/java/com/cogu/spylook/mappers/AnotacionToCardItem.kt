package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.Anotacion
import org.mapstruct.Mapper

@Mapper
interface AnotacionToCardItem {
    fun toCardItem(anotacion: Anotacion): AnotacionCardItem?
}