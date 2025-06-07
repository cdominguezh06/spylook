package com.cogu.domain.relations

import com.cogu.domain.model.Contacto
import com.cogu.domain.model.Grupo

data class CreadorConGrupos(
    val creador : Contacto,
    val grupos : List<Grupo>
)