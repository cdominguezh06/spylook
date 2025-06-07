package com.cogu.domain.relations

import com.cogu.domain.model.Contacto
import com.cogu.domain.model.Grupo

data class ContactosConGrupos(
    val contacto : Contacto,
    val grupos : List<Grupo>
)