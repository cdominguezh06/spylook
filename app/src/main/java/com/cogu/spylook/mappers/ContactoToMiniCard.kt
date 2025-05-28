package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.model.entity.Contacto
import org.mapstruct.Mapper

@Mapper
interface ContactoToMiniCard {
    fun toMiniCard(contacto: Contacto): ContactoMiniCard
}