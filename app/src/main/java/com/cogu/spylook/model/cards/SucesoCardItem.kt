package com.cogu.spylook.model.cards

import com.cogu.data.DateConverters
import java.time.LocalDateTime

class SucesoCardItem(
    @JvmField var idAnotable: Int,
    @JvmField var nombre: String,
    @JvmField var fecha: String,
    @JvmField var implicados: String = "",
    @JvmField var colorFoto: Int,
    @JvmField var clickable: Boolean = true
) {
    companion object{

        val DEFAULT_FOR_NO_RESULTS = SucesoCardItem(
            idAnotable = -1,
            fecha = DateConverters.toDateTimeString(LocalDateTime.now()),
            nombre = "Sin resultados",
            implicados = "Sin implicados",
            colorFoto = 0,
            clickable = false
        )

        val DEFAULT_FOR_ADD = SucesoCardItem(
            idAnotable = -1,
            fecha = DateConverters.toDateTimeString(LocalDateTime.now()),
            nombre = "Agregar suceso",
            implicados = "Sin implicados",
            colorFoto = 0,
            clickable = false
        )

    }
}