package com.yjotdev.clasificarpeces.domain.repository

import kotlinx.coroutines.flow.Flow
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

interface SpeciesDaoRepository {
    suspend fun insertSpecies(items: List<SpeciesModel>)

    fun getAllSpecies(): Flow<List<SpeciesModel>>

    fun searchBy(query: String): Flow<List<SpeciesModel>>
}