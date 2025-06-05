package com.cogu.domain.model

data class Grupo(
    override var idAnotable: Int = 0,
    override var nombre: String,
    var colorFoto : Int,
    var idCreador: Int = 0,
): Anotable(idAnotable, nombre) {
    constructor() : this(0, "", 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grupo

        if (idAnotable != other.idAnotable) return false
        if (colorFoto != other.colorFoto) return false
        if (idCreador != other.idCreador) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idAnotable
        result = 31 * result + colorFoto
        result = 31 * result + idCreador
        result = 31 * result + nombre.hashCode()
        return result
    }

}