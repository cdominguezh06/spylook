package com.cogu.spylook.model.cards

data class ContactoCardItem(
    var idAnotable: Int,
    var nombre: String,
    var alias: String,
    var colorFoto: Int,
    var clickable: Boolean = true
) {

    companion object{
        val DEFAULT_FOR_EMPTY_LIST = ContactoCardItem(
            idAnotable = -1,
            nombre = "Vaya...",
            alias = "Que vacío todo",
            colorFoto = 0,
            clickable = false
        )

        val DEFAULT_FOR_NO_RESULTS = ContactoCardItem(
            -1,
            "",
            "Sin resultados",
            0,
            false
        )

        val DEFAULT_FOR_SEARCH = ContactoCardItem(
            -1,
            "Pulsa para...",
            "Buscar",
            0,
            true
        )

    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactoCardItem

        if (idAnotable != other.idAnotable) return false
        if (colorFoto != other.colorFoto) return false
        if (clickable != other.clickable) return false
        if (nombre != other.nombre) return false
        if (alias != other.alias) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idAnotable
        result = 31 * result + colorFoto
        result = 31 * result + clickable.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + alias.hashCode()
        return result
    }
}
