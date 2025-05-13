package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "contactos",
    indices = [androidx.room.Index(value = ["nombre", "alias"], unique = true)]
)
data class Contacto(
    @PrimaryKey(autoGenerate = true)
    val idContacto: Int = 0,
    var nombre: String?,
    var alias: String?,
    var fechaNacimiento: LocalDate?,
    var ciudad: String?,
    var estado: String?,
    var pais: String?,
    var colorFoto: Int
) {
    @Ignore
    var edad: Int = calcularEdad(fechaNacimiento)

    @Ignore
    constructor(
        nombre: String?,
        alias: String?,
        fechaNacimiento: LocalDate,
        ciudad: String?,
        estado: String?,
        pais: String?,
        colorFoto: Int
    ) : this(
        idContacto = 0,
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
                if (LocalDate.now().isAfter(
                        fechaNacimiento.withYear(LocalDate.now().year)
                            .withDayOfYear(LocalDate.now().dayOfYear - 1)
                    )
                ) {
                    LocalDate.now().year - fechaNacimiento.year
                } else {
                    LocalDate.now().year - (fechaNacimiento.year - 1)
                }
            } else 0
        }
    }
}