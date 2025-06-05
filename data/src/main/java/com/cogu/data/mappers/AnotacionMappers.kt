package com.cogu.data.mappers

import com.cogu.data.entity.AnotacionEntity
import com.cogu.domain.model.Anotacion

fun Anotacion.toEntity(): AnotacionEntity {
    return AnotacionEntity(
        id = id,
        fecha = fecha,
        titulo = titulo,
        descripcion = descripcion,
        idAnotable = idAnotable
    )
}

fun AnotacionEntity.toModel(): Anotacion {
    return Anotacion(
        id = id,
        fecha = fecha,
        titulo = titulo,
        descripcion = descripcion,
        idAnotable = idAnotable
    )
}