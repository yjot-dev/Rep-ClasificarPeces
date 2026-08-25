package com.yjotdev.clasificarpeces.domain.usecase

import javax.inject.Inject
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.SpeciesRepository
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

class SpeciesUseCase @Inject constructor(
    private val speciesRepository: SpeciesRepository
) {
    suspend operator fun invoke(
        searchedText: String? = null,
        language: String
    ): Result<List<SpeciesModel>> {
        return speciesRepository.seleccionarEspecies(searchedText, language)
    }
}