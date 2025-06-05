package com.cogu.spylook.mappers

import com.cogu.domain.model.Contacto
import com.cogu.spylook.model.cards.ContactoCardItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface ContactoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(contacto: Contacto): ContactoCardItem
}
