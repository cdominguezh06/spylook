package com.cogu.spylook.model.utils.converters

import android.content.Context
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.model.SingleExportObject
import com.cogu.spylook.model.entity.Contacto
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors

object ContactoToSingleExportObjectConverter {

    suspend fun toSingleExportObject(
        contacto: Contacto,
        context: Context,
        deepLevel: Int = 1,
        excluded: MutableList<Contacto> = mutableListOf()
    ): SingleExportObject {
        val db = AppDatabase.getInstance(context)
        val anotaciones = db!!.anotacionDAO()!!.getAnotacionesContacto(contacto.id)
        val sucesos = db.sucesoDAO()!!.getContactosWithSucesos()
            .stream()
            .filter { c -> c.contacto!! == contacto }
            .map { c -> c.sucesos }
            .findFirst()
            .get()
        if (deepLevel > 0) {
            excluded.add(contacto)
            var amistades = db.contactoDAO()!!.getAmigosDeContacto(contacto.id)
                .amigos
                ?.stream()
                ?.filter { c -> !excluded.contains(c) }
                ?.map { amigo ->
                    runBlocking {
                        toSingleExportObject(amigo!!, context, deepLevel - 1, excluded)
                    }
                }
                ?.collect(Collectors.toList())
            return SingleExportObject(contacto, anotaciones, sucesos, amistades!!)
        }
        return SingleExportObject(contacto, anotaciones, sucesos, mutableListOf())
    }
}