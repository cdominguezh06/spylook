package com.cogu.spylook.mappers

import com.cogu.domain.model.Contacto
import com.cogu.spylook.model.cards.ContactoMiniCard
import org.mapstruct.Mapper

@Mapper
interface ContactoToMiniCard {
    fun toMiniCard(contacto: Contacto): ContactoMiniCard
}