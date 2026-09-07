package com.yjotdev.clasificarpeces.domain.repository

import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.core.Result

interface SpeciesApiRepository {
    suspend fun seleccionarEspecies(language: String): Result<List<SpeciesModel>>
}