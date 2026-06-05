package com.yjotdev.clasificarpeces.utils.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.model.ImageModel

@Singleton
class FakeClassifierRepositoryImpl @Inject constructor() : ClassifierRepository{
    override suspend fun classifier(image: ImageModel): Result<List<ClassifierModel>> {
        return if (image.image.isNotEmpty()) {
            // Simulamos una respuesta exitosa inmediata
            val fakeData = listOf(
                ClassifierModel(
                    label = "Betta",
                    score = 0.99f
                ),
                ClassifierModel(
                    label = "Guppy",
                    score = 0.23f
                ),
                ClassifierModel(
                    label = "Molly",
                    score = 0.06f
                )
            )
            Result.Success(fakeData)
        } else {
            Result.Error(Exception("Error al clasificar imagen"))
        }
    }
}