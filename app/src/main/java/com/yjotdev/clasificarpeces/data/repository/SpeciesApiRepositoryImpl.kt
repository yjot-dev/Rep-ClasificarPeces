package com.yjotdev.clasificarpeces.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.core.mapSuccess
import com.yjotdev.clasificarpeces.data.remote.api.SpeciesApi
import com.yjotdev.clasificarpeces.data.remote.core.safeApiCallForBody
import com.yjotdev.clasificarpeces.data.remote.mapper.toDomain
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

@Singleton
class SpeciesApiRepositoryImpl @Inject constructor(
    private val speciesApi: SpeciesApi
) : SpeciesApiRepository {

    override suspend fun seleccionarEspecies(language: String): Result<List<SpeciesModel>> {
        return safeApiCallForBody { speciesApi.seleccionarEspecies(language) }
            .mapSuccess { result -> result.map { it.toDomain() } }
    }
}