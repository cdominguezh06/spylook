package com.cogu.spylook.mappers;

import com.cogu.spylook.model.cards.ContactoCardItem;
import com.cogu.spylook.model.Contacto;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper
public interface ContactoToCardItem {
    ContactoCardItem toCardItem(Contacto contacto);

    @ObjectFactory
    default ContactoCardItem createCardItem(Contacto contacto){
        return new ContactoCardItem(contacto.getId(), contacto.getNombre(), contacto.getNickMasConocido(), contacto.getEdad());
    }
}
