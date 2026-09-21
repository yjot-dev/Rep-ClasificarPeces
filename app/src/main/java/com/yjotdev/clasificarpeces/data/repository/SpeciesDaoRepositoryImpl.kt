package com.yjotdev.clasificarpeces.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yjotdev.clasificarpeces.data.local.dao.SpeciesDao
import com.yjotdev.clasificarpeces.data.local.mapper.toBD
import com.yjotdev.clasificarpeces.data.local.mapper.toDomain
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository
import com.yjotdev.clasificarpeces.domain.utils.Helper

@Singleton
class SpeciesDaoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val speciesDao: SpeciesDao
): SpeciesDaoRepository {
    override suspend fun insertSpecies(items: List<SpeciesModel>) {
        speciesDao.insertSpecies(items.map { it.copy(
            imageUri = Helper.saveImageLocally(context, it.imageUri, it.id.toString())
        ).toBD() })
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