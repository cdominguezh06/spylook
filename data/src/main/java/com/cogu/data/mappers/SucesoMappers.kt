package com.cogu.data.mappers

import com.cogu.data.entity.SucesoEntity
import com.cogu.domain.model.Suceso

fun Suceso.toEntity(): SucesoEntity {
    return SucesoEntity(
        idAnotable = idAnotable,
        nombre = nombre,
        fecha = fecha,
        lugar = lugar,
        descripcion = descripcion,
        idCausante = idCausante,
        colorFoto = colorFoto
    )
}

fun SucesoEntity.toModel(): Suceso {
    return Suceso(
        idAnotable = idAnotable,
        nombre = nombre,
        fecha = fecha,
        lugar = lugar,
        descripcion = descripcion,
        idCausante = idCausante,
        colorFoto = colorFoto
    )
}