package com.cogu.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import java.time.LocalDate

@Entity(
    tableName = "contactos",
    indices = [androidx.room.Index(value = ["alias"], unique = true)]
)
class ContactoEntity(
    idAnotable: Int = 0,
    nombre: String,
    var alias: String,
    var fechaNacimiento: LocalDate,
    var ciudad: String,
    var estado: String,
    var pais: String,
    var colorFoto: Int,
): AnotableEntity(idAnotable, nombre) {
    @Ignore
    var edad: Int = calcularEdad(fechaNacimiento)

    @Ignore
    constructor(
        nombre: String,
        alias: String,
        fechaNacimiento: LocalDate,
        ciudad: String,
        estado: String,
        pais: String,
        colorFoto: Int
    ) : this(
        idAnotable = 0,
        nombre = nombre,
        alias = alias,
        fechaNacimiento = fechaNacimiento,
        ciudad = ciudad,
        estado = estado,
        pais = pais,
        colorFoto = colorFoto
    )

    companion object {
        private fun calcularEdad(fechaNacimiento: LocalDate?): Int {
            return if (fechaNacimiento != null) {
                if (LocalDate.now()
                    .isAfter(
                        fechaNacimiento
                            .withYear(LocalDate.now().year)
                            .minusDays(1)
                    )
                ) {
                    LocalDate.now().year - fechaNacimiento.year
                } else {
                    (LocalDate.now().year - (fechaNacimiento.year))- 1
                }
            } else 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactoEntity

        if (colorFoto != other.colorFoto) return false
        if (edad != other.edad) return false
        if (alias != other.alias) return false
        if (fechaNacimiento != other.fechaNacimiento) return false
        if (ciudad != other.ciudad) return false
        if (estado != other.estado) return false
        if (pais != other.pais) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colorFoto
        result = 31 * result + edad
        result = 31 * result + alias.hashCode()
        result = 31 * result + fechaNacimiento.hashCode()
        result = 31 * result + ciudad.hashCode()
        result = 31 * result + estado.hashCode()
        result = 31 * result + pais.hashCode()
        return result
    }

}