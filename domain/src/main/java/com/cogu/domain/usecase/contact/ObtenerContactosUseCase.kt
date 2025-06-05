package com.cogu.domain.usecase.contact

import com.cogu.domain.model.Contacto
import com.cogu.domain.repository.ContactoRepository
import kotlinx.coroutines.flow.Flow

class ObtenerContactosUseCase(private val repo: ContactoRepository) {
    operator fun invoke(): Flow<List<Contacto>> = repo.getContactos()
}