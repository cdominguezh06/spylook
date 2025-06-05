package com.cogu.domain.repository

import com.cogu.domain.model.Contacto
import kotlinx.coroutines.flow.Flow

interface ContactoRepository {
    fun getContactos() : Flow<List<Contacto>>
}