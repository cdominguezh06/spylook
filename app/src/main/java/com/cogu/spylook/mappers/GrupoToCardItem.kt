package com.cogu.spylook.mappers

import com.cogu.domain.model.Grupo
import com.cogu.spylook.model.cards.GrupoCardItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface GrupoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(grupo: Grupo): GrupoCardItem
}
