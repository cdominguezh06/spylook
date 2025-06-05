package com.cogu.domain.model

data class Suceso(
    override var idAnotable: Int,
    override var nombre: String,
    var fecha: String,
    var lugar: String,
    var descripcion: String,
    var idCausante: Int,
    var colorFoto: Int
) : Anotable(idAnotable, nombre){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Suceso

        if (idAnotable != other.idAnotable) return false
        if (idCausante != other.idCausante) return false
        if (colorFoto != other.colorFoto) return false
        if (nombre != other.nombre) return false
        if (fecha != other.fecha) return false
        if (lugar != other.lugar) return false
        if (descripcion != other.descripcion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idAnotable
        result = 31 * result + idCausante
        result = 31 * result + colorFoto
        result = 31 * result + nombre.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + lugar.hashCode()
        result = 31 * result + descripcion.hashCode()
        return result
    }
}