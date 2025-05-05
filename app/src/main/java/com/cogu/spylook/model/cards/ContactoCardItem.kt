package com.cogu.spylook.model.cards

data class ContactoCardItem(
    var id: Int,
    var nombre: String?,
    var alias: String?,
    var foto: Int,
    var clickable: Boolean = true
) {
}
