package com.cogu.spylook.model.cards

data class GrupoCardItem(
    var idAnotable: Int,
    var nombre: String,
    var clickable: Boolean = true
){
    companion object{
        val DEFAULT_FOR_EMPTY_LIST = GrupoCardItem(
            idAnotable = -1,
            nombre = "Que vacío todo",
            clickable = false
        )
    }
}