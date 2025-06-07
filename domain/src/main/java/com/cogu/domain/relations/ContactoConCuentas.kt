package com.cogu.domain.relations

import com.cogu.domain.model.Contacto
import com.cogu.domain.model.Cuenta

data class ContactoConCuentas(
    val contacto : Contacto,
    val cuentas : List<Cuenta>
)
