package com.yjotdev.clasificarpeces.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.data.local.dao.SpeciesDao
import com.yjotdev.clasificarpeces.data.local.mapper.toBD
import com.yjotdev.clasificarpeces.data.local.mapper.toDomain
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository

@Singleton
class SpeciesDaoRepositoryImpl @Inject constructor(
    private val speciesDao: SpeciesDao
): SpeciesDaoRepository {
    override suspend fun insertSpecies(items: List<SpeciesModel>) {
        speciesDao.insertSpecies(items.map { it.toBD() })
    }

    override fun getAllSpecies(): Flow<List<SpeciesModel>> {
        return speciesDao.getAllSpecies().map { listFromDb ->
            listFromDb.map { it.toDomain() }
        }
    }

    override fun searchBy(query: String): Flow<List<SpeciesModel>> {
        return speciesDao.searchBy(query).map { listFromDb ->
            listFromDb.map { it.toDomain() }
        }
    }
}