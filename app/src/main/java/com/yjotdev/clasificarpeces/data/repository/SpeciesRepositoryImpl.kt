package com.yjotdev.clasificarpeces.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.SpeciesRepository
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.core.mapSuccess
import com.yjotdev.clasificarpeces.data.remote.api.SpeciesApi
import com.yjotdev.clasificarpeces.data.remote.core.safeApiCallForBody
import com.yjotdev.clasificarpeces.data.remote.mapper.toDomain
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

@Singleton
class SpeciesRepositoryImpl @Inject constructor(
    private val speciesApi: SpeciesApi
) : SpeciesRepository {

    override suspend fun seleccionarEspecies(searchedText: String?, language: String)
    : Result<List<SpeciesModel>> {
        return safeApiCallForBody { speciesApi.seleccionarEspecies(searchedText, language) }
            .mapSuccess { result -> result.map { it.toDomain() } }
    }
}