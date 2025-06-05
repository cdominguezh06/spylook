package com.cogu.domain.model

import utils.DateUtils
import java.time.LocalDate

data class Contacto(
    override var idAnotable: Int = 0,
    override var nombre: String,
    var alias: String,
    var fechaNacimiento: LocalDate,
    var ciudad: String,
    var estado: String,
    var pais: String,
    var colorFoto: Int,
    var edad: Int = DateUtils.calcularEdad(fechaNacimiento)
) : Anotable(idAnotable, nombre) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contacto

        if (idAnotable != other.idAnotable) return false
        if (colorFoto != other.colorFoto) return false
        if (edad != other.edad) return false
        if (nombre != other.nombre) return false
        if (alias != other.alias) return false
        if (fechaNacimiento != other.fechaNacimiento) return false
        if (ciudad != other.ciudad) return false
        if (estado != other.estado) return false
        if (pais != other.pais) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idAnotable
        result = 31 * result + colorFoto
        result = 31 * result + edad
        result = 31 * result + nombre.hashCode()
        result = 31 * result + alias.hashCode()
        result = 31 * result + fechaNacimiento.hashCode()
        result = 31 * result + ciudad.hashCode()
        result = 31 * result + estado.hashCode()
        result = 31 * result + pais.hashCode()
        return result
    }
}