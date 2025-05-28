package com.cogu.spylook.model.cards

data class GrupoCardItem(
    var idAnotable: Int,
    var nombre: String,
    var colorFoto: Int,
    var miembros : Int,
    var clickable: Boolean = true
){
    companion object{
        val DEFAULT_FOR_EMPTY_LIST = GrupoCardItem(
            idAnotable = -1,
            nombre = "Que vacío todo",
            colorFoto = 0,
            miembros = 0,
            clickable = false
        )

        val DEFAULT_FOR_NO_RESULTS = GrupoCardItem(
            -1,
            "Sin resultados",
            0,
            0,
            false
        )

        val ADD_TO_GROUP = GrupoCardItem(
            idAnotable = -1,
            nombre = "Añadir a grupo",
            clickable = false,
            colorFoto = 0,
            miembros = 0
        )
    }
}