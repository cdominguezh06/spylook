package com.cogu.domain.relations

import com.cogu.domain.model.Contacto
import com.cogu.domain.model.Grupo

data class GruposConContactos(
    val grupo: Grupo,
    val contactos: List<Contacto>
)