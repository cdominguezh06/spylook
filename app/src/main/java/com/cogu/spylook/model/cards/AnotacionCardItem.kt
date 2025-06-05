package com.cogu.spylook.model.cards

import com.cogu.data.DateConverters
import java.time.LocalDateTime

data class AnotacionCardItem(
    val id : Int,
    val titulo : String,
    val descripcion : String,
    val fecha : String,
    var idAnotable : Int
) {
    companion object{
        val DEFAULT_FOR_NEW = AnotacionCardItem(
                id = -1,
                titulo = "Nueva Anotacion",
                descripcion = "",
                fecha = DateConverters.toDateTimeString(LocalDateTime.now()),
                idAnotable = -1
            )
        fun getDefaultForNew(idAnotable : Int) : AnotacionCardItem{
            DEFAULT_FOR_NEW.idAnotable = idAnotable
            return DEFAULT_FOR_NEW
        }
    }
}