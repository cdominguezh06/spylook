package com.cogu.data.mappers

import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.model.entity.ContactoEntity
import org.mapstruct.Mapper

@Mapper
interface ContactoToMiniCard {
    fun toMiniCard(contactoEntity: ContactoEntity): ContactoMiniCard
}