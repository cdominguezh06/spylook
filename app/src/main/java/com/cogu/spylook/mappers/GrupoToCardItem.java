package com.cogu.spylook.mappers;

import com.cogu.spylook.model.Grupo;
import com.cogu.spylook.model.cards.GrupoCardItem;

import org.mapstruct.Mapper;

@Mapper
public interface GrupoToCardItem {
    GrupoCardItem toCardItem(Grupo grupo);
}
