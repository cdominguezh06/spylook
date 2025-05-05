package com.cogu.spylook.mappers

import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Grupo
import org.mapstruct.Mapper

@Mapper
interface GrupoToCardItem {
    fun toCardItem(grupo: Grupo?): GrupoCardItem?
}
