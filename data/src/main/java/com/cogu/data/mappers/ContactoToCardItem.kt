package com.cogu.data.mappers

import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.ContactoEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface ContactoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(contactoEntity: ContactoEntity): ContactoCardItem
}
