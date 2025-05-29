package com.cogu.spylook.model.cards

import com.cogu.spylook.model.utils.converters.DateConverters
import java.time.LocalDateTime

class CuentaCardItem(
    @JvmField var idAnotable: Int,
    @JvmField var nombre: String,
    @JvmField var link: String,
    @JvmField var redSocial: String,
    @JvmField var colorFoto: Int,
    @JvmField var clickable: Boolean = true
){
    companion object{
        val DEFAULT_FOR_NO_RESULTS = CuentaCardItem(
            idAnotable = -1,
            nombre = "Sin resultados",
            link = "",
            redSocial = "",
            colorFoto = 0,
            clickable = false
        )
        val DEFAULT_FOR_ADD = CuentaCardItem(
            idAnotable = -1,
            nombre = "Agregar cuenta",
            link = "",
            redSocial = "",
            colorFoto = 0,
            clickable = false
        )
    }
}