package com.yjotdev.clasificarpeces.domain.repository

import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.core.Result

interface SpeciesRepository {
    suspend fun seleccionarEspecies(
        searchedText: String? = null,
        language: String
    ): Result<List<SpeciesModel>>
}