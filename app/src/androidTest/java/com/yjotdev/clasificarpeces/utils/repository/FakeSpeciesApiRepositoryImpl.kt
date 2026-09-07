package com.yjotdev.clasificarpeces.utils.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

@Singleton
class FakeSpeciesApiRepositoryImpl @Inject constructor() : SpeciesApiRepository{
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

    override suspend fun seleccionarEspecies(language: String): Result<List<SpeciesModel>> {
        // Simulamos una respuesta exitosa
        return Result.Success(fakeData)
    }
}