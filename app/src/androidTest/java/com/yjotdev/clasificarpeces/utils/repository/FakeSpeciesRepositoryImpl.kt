package com.yjotdev.clasificarpeces.utils.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.SpeciesRepository
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

@Singleton
class FakeSpeciesRepositoryImpl @Inject constructor() : SpeciesRepository{
    override suspend fun seleccionarEspecies(searchedText: String?, language: String)
    : Result<List<SpeciesModel>> {
        return if (searchedText.isNullOrEmpty()) {
            // Simulamos una respuesta exitosa inmediata
            val fakeData = listOf(
                SpeciesModel(commonName = "Molly", scientificName = "Poecilia sphenops", imageUri = "https://th.bing.com/th/id/R.c66dbc83e0259bc3ca50dfe95474496e?rik=4xnw5BacdNyy5g&pid=ImgRaw&r=0"),
                SpeciesModel(commonName = "Platy", scientificName = "Xiphophorus maculatus", imageUri = "https://tse2.mm.bing.net/th/id/OIP.-JPNn34XLyMhKTLSgJRVIQHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3")
            )
            Result.Success(fakeData)
        } else {
            Result.Error(Exception("Error al obtener imagen"))
        }
    }
}