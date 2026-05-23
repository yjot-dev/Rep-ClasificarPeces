package com.yjotdev.clasificarpeces.utils.repository

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.core.Result

@Singleton
class FakeClassifierRepository @Inject constructor() : ClassifierRepository{
    override suspend fun classify(image: Bitmap): Result<List<ClassifierModel>> {
        // Simulamos una respuesta exitosa inmediata sin usar TFLite
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
        return Result.Success(fakeData)
    }
}