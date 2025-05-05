package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import org.mapstruct.Mapper
import org.mapstruct.ObjectFactory

@Mapper
interface ContactoToCardItem {
    fun toCardItem(contacto: Contacto?): ContactoCardItem?

    @ObjectFactory
    fun createCardItem(contacto: Contacto): ContactoCardItem {
        return ContactoCardItem(contacto.id, contacto.nombre, contacto.alias, contacto.edad)
    }
}
