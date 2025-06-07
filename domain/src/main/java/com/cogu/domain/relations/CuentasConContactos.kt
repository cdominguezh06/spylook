package com.cogu.domain.relations

import com.cogu.domain.model.Contacto
import com.cogu.domain.model.Cuenta

data class CuentasConContactos(
    val cuenta : Cuenta,
    val contactos : List<Contacto>
)