package com.cogu.data.mappers

import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.GrupoEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface GrupoToCardItem {
    @Mapping(target = "clickable", constant = "true")
    fun toCardItem(grupoEntity: GrupoEntity): GrupoCardItem
    fun toGrupo(grupoCardItem: GrupoCardItem): GrupoEntity
}
