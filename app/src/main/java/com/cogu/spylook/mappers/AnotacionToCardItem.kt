package com.cogu.spylook.mappers

import com.cogu.domain.model.Anotacion
import com.cogu.spylook.model.cards.AnotacionCardItem
import org.mapstruct.Mapper

@Mapper
interface AnotacionToCardItem {
    fun toCardItem(anotacion: Anotacion): AnotacionCardItem
}