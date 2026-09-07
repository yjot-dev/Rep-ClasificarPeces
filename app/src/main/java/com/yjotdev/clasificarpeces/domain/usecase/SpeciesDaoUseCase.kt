package com.yjotdev.clasificarpeces.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository

class SpeciesDaoUseCase @Inject constructor(
    private val speciesDaoRepository: SpeciesDaoRepository
) {
    suspend operator fun invoke(items: List<SpeciesModel>) {
        return speciesDaoRepository.insertSpecies(items)
    }

    operator fun invoke(): Flow<List<SpeciesModel>> {
        return speciesDaoRepository.getAllSpecies()
    }

    operator fun invoke(query: String): Flow<List<SpeciesModel>> {
        return speciesDaoRepository.searchBy(query)
    }
}