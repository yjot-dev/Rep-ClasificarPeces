package com.yjotdev.clasificarpeces.domain.usecase

import javax.inject.Inject
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

class SpeciesApiUseCase @Inject constructor(
    private val speciesApiRepository: SpeciesApiRepository
) {
    suspend operator fun invoke(language: String): Result<List<SpeciesModel>> {
        return speciesApiRepository.seleccionarEspecies(language)
    }
}