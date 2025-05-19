package com.cogu.spylook.model.cards

data class ContactoCardItem(
    var idContacto: Int,
    var nombre: String,
    var alias: String,
    var colorFoto: Int,
    var clickable: Boolean = true
) {
}
