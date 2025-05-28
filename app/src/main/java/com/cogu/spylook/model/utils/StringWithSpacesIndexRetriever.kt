package com.cogu.spylook.model.utils

import android.util.Log

class StringWithSpacesIndexRetriever {

    var contador = 0
    var ultimaBusquedaLength = 0
    private var indicesDeEspacios = listOf<Int>()
    fun getSpanIntervalJump(busqueda: String, texto: String, startIndex: Int): Int {
        contador = 0
        val indexUltimaLetra = busqueda.length - 1
        indicesDeEspacios = indicesDeEspacios.filter { it > startIndex }
        indicesDeEspacios.forEach {
            if (indexUltimaLetra+contador > 0 && startIndex+indexUltimaLetra+contador >= it) {
                contador += 1
            }
        }
        if (contador < 0) {
            contador = 0
        }
        ultimaBusquedaLength = busqueda.length
        var fin = startIndex + busqueda.length + contador
        if (fin > texto.length) {
            fin = texto.length
            contador -= 1
        }
        return fin
    }

    fun getStartIndex(busqueda: String, texto: String): Int {
        contador = 0
        val indexUltimaLetra = texto.replace(" ", "").lowercase().indexOf(busqueda)
        indicesDeEspacios = texto.mapIndexedNotNull { index, c -> index.takeIf { c == ' ' } }
        indicesDeEspacios.forEach {
            if (indexUltimaLetra+contador >= it) {
                contador += 1
            }
        }
        return indexUltimaLetra+contador
    }
}