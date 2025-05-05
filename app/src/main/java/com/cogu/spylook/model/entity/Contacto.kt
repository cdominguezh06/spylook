package com.cogu.spylook.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import lombok.NoArgsConstructor
import java.time.LocalDate


@NoArgsConstructor
@Entity(tableName = "contactos")
class Contacto {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @JvmField
    var nombre: String?
    @JvmField
    var alias: String?

    @JvmField
    @Ignore
    var edad: Int
    @JvmField
    var foto: Int = 0
    @JvmField
    var fechaNacimiento: LocalDate?
    @JvmField
    var ciudad: String?
    @JvmField
    var estado: String?
    @JvmField
    var pais: String?

    constructor(
        id: Int,
        nombre: String?,
        alias: String?,
        fechaNacimiento: LocalDate,
        ciudad: String?,
        estado: String?,
        pais: String?
    ) {
        this.id = id
        this.nombre = nombre
        this.alias = alias
        this.fechaNacimiento = fechaNacimiento
        this.edad = if (LocalDate.now().isAfter(
                fechaNacimiento.withYear(LocalDate.now().getYear())
                    .withDayOfYear(LocalDate.now().getDayOfYear() - 1)
            )
        )
            LocalDate.now().getYear() - fechaNacimiento.getYear()
        else
            LocalDate.now().getYear() - (fechaNacimiento.getYear() - 1)
        this.ciudad = ciudad
        this.estado = estado
        this.pais = pais
    }

    @Ignore
    constructor(
        nombre: String?,
        alias: String?,
        fechaNacimiento: LocalDate,
        ciudad: String?,
        estado: String?,
        pais: String?
    ) {
        this.nombre = nombre
        this.alias = alias
        this.fechaNacimiento = fechaNacimiento
        this.edad = if (LocalDate.now().isAfter(
                fechaNacimiento.withYear(LocalDate.now().getYear())
                    .withDayOfYear(LocalDate.now().getDayOfYear() - 1)
            )
        )
            LocalDate.now().getYear() - fechaNacimiento.getYear()
        else
            LocalDate.now().getYear() - (fechaNacimiento.getYear() - 1)
        this.ciudad = ciudad
        this.estado = estado
        this.pais = pais
    }
}
