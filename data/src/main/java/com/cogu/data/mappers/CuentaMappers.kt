package com.cogu.data.mappers

import com.cogu.data.entity.CuentaEntity
import com.cogu.domain.model.Cuenta

fun Cuenta.toEntity(): CuentaEntity {
    return CuentaEntity(
        idAnotable = idAnotable,
        nombre = nombre,
        link = link,
        redSocial = redSocial,
        colorFoto = colorFoto,
        idPropietario = idPropietario
    )
}

fun CuentaEntity.toModel(): Cuenta {
    return Cuenta(
        idAnotable = idAnotable,
        nombre = nombre,
        link = link,
        redSocial = redSocial,
        colorFoto = colorFoto,
        idPropietario = idPropietario
    )
}