package com.cogu.domain.usecase

import com.cogu.domain.model.Contacto
import kotlinx.coroutines.flow.Flow

class ObtenerContactosUseCase(private val repo: ContactoRepository) {
    operator fun invoke(): Flow<List<Contacto>> = repo.getContactos()
}