package utils

import java.time.LocalDate

object DateUtils {

    fun calcularEdad(fechaNacimiento: LocalDate?): Int {
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
                (LocalDate.now().year - (fechaNacimiento.year)) - 1
            }
        } else 0
    }
}