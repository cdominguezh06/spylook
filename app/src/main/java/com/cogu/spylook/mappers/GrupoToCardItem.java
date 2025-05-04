package com.cogu.spylook.mappers;

import com.cogu.spylook.model.entity.Grupo;
import com.cogu.spylook.model.cards.GrupoCardItem;

import org.mapstruct.Mapper;

@Mapper
public interface GrupoToCardItem {
    GrupoCardItem toCardItem(Grupo grupo);
}
