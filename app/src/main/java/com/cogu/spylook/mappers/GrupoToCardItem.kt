package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Grupo
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface GrupoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(grupo: Grupo): GrupoCardItem
    fun toGrupo(grupoCardItem: GrupoCardItem): Grupo
}
