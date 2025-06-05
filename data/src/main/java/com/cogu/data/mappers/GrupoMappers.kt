package com.cogu.data.mappers

import com.cogu.data.entity.GrupoEntity
import com.cogu.domain.model.Grupo

fun Grupo.toEntity(): GrupoEntity {
    return GrupoEntity(
        idAnotable,
        nombre,
        colorFoto,
        idCreador
    )
}

fun GrupoEntity.toModel(): Grupo{
    return Grupo(
        idAnotable,
        nombre,
        colorFoto,
        idCreador
    )
}