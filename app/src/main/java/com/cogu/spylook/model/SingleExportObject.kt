package com.cogu.spylook.model

import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Suceso

class SingleExportObject(
    val contacto: Contacto,
    val anotaciones: MutableList<Anotacion>,
    val sucesos: MutableList<Suceso?>,
    val amistades: List<SingleExportObject?>
)