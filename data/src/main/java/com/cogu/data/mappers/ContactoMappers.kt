package com.cogu.data.mappers

import com.cogu.data.entity.ContactoEntity
import com.cogu.domain.model.Contacto

fun Contacto.toEntity(): ContactoEntity {
    return ContactoEntity(
        idAnotable,
        nombre,
        alias,
        fechaNacimiento,
        ciudad,
        estado,
        pais,
        colorFoto
    )
}

fun ContactoEntity.toModel(): Contacto{
    return Contacto(
        idAnotable,
        nombre,
        alias,
        fechaNacimiento,
        ciudad,
        estado,
        pais,
        colorFoto
    )
}