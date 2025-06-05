package com.cogu.domain.model

data class Cuenta(
    override var idAnotable: Int,
    override var nombre: String,
    var link: String,
    var redSocial: String,
    var colorFoto: Int,
    var idPropietario: Int
) : Anotable(idAnotable, nombre){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cuenta

        if (idAnotable != other.idAnotable) return false
        if (colorFoto != other.colorFoto) return false
        if (idPropietario != other.idPropietario) return false
        if (nombre != other.nombre) return false
        if (link != other.link) return false
        if (redSocial != other.redSocial) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idAnotable
        result = 31 * result + colorFoto
        result = 31 * result + idPropietario
        result = 31 * result + nombre.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + redSocial.hashCode()
        return result
    }
}