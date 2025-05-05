package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface ContactoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(contacto: Contacto?): ContactoCardItem?
}
