package com.cogu.spylook.model.cards

data class GrupoCardItem(
    var idGrupo: Int,
    var nombre: String,
    var colorFoto: Int,
    var clickable: Boolean = true
)