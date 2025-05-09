package com.cogu.spylook.model.utils.converters

import android.content.Context
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.model.SingleExportObject
import com.cogu.spylook.model.entity.Contacto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object ContactoToSingleExportObjectConverter {

    suspend fun toSingleExportObject(
        contacto: Contacto,
        context: Context,
        deepLevel: Int = 1,
        excluded: List<Contacto> = listOf()
    ): SingleExportObject = coroutineScope {
        val db = AppDatabase.getInstance(context)
        val anotaciones = db?.anotacionDAO()?.getAnotacionesContacto(contacto.id)
        val sucesos = db?.sucesoDAO()?.getContactosWithSucesos()
            ?.filter { it.contacto == contacto }
            ?.map { it.sucesos }
            ?.firstOrNull() ?: emptyList()

        if (deepLevel > 0) {
            val newExcluded = excluded + contacto
            val amistades = db?.contactoDAO()?.getAmigosDeContacto(contacto.id)
                ?.amigos
                ?.filter { it !in newExcluded }
                ?.map { amigo ->
                    async { toSingleExportObject(amigo!!, context, deepLevel - 1, newExcluded) }
                }
                ?.map { it.await() }
                ?: emptyList()

            return@coroutineScope SingleExportObject(contacto, anotaciones, sucesos, amistades)
        }

        return@coroutineScope SingleExportObject(contacto, anotaciones, sucesos, emptyList())
    }
}