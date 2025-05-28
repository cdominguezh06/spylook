package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "contactos",
    indices = [androidx.room.Index(value = ["alias"], unique = true)]
)
class Contacto(
    idAnotable: Int = 0,
    nombre: String,
    @JvmField
    var alias: String,
    @JvmField
    var fechaNacimiento: LocalDate,
    @JvmField
    var ciudad: String,
    @JvmField
    var estado: String,
    @JvmField
    var pais: String,
    @JvmField
    var colorFoto: Int,
): Anotable(idAnotable, nombre) {
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
                if (LocalDate.now().isAfter(
                        fechaNacimiento.withYear(LocalDate.now().year)
                    )
                ) {
                    LocalDate.now().year - fechaNacimiento.year
                } else {
                    (LocalDate.now().year - (fechaNacimiento.year))- 1
                }
            } else 0
        }
    }
}