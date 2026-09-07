package com.yjotdev.clasificarpeces.utils.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository

@Singleton
class FakeSpeciesDaoRepositoryImpl @Inject constructor() : SpeciesDaoRepository {
    private var fakeData = listOf(
        SpeciesModel(
            id = 1,
            type = "pez",
            commonName = "Pez Payaso",
            scientificName = "Balaenoptera physalus"
        ),
        SpeciesModel(
            id = 2,
            type = "tortuga",
            commonName = "Tortuga de orejas rojas",
            scientificName = "Asads weree"
        ),
        SpeciesModel(
            id = 3,
            type = "caracol",
            commonName = "Caracol asesino",
            scientificName = "Carassius auratus"
        )
    )

    override suspend fun insertSpecies(items: List<SpeciesModel>) {
        if (!fakeData.containsAll(items)) {
            fakeData = items
        }
    }

    override fun getAllSpecies(): Flow<List<SpeciesModel>> {
        return flow { emit(fakeData) }
    }

    override fun searchBy(query: String): Flow<List<SpeciesModel>> {
        return flow {
            val filtered = fakeData.filter {
                it.commonName.contains(query, ignoreCase = true)
            }
            emit(filtered)
        }
    }
}