package com.cogu.domain.model

data class Anotacion(
    var id: Int = 0,
    var fecha: String,
    var titulo: String,
    var descripcion: String,
    var idAnotable: Int = 0
) {
    constructor() : this(0, "", "", "", 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Anotacion

        if (id != other.id) return false
        if (idAnotable != other.idAnotable) return false
        if (fecha != other.fecha) return false
        if (titulo != other.titulo) return false
        if (descripcion != other.descripcion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idAnotable
        result = 31 * result + fecha.hashCode()
        result = 31 * result + titulo.hashCode()
        result = 31 * result + descripcion.hashCode()
        return result
    }


}