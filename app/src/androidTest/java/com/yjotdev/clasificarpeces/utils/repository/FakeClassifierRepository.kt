package com.yjotdev.clasificarpeces.utils.repository

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.port.ClassifierPort
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity
import com.yjotdev.clasificarpeces.domain.core.Result

@Singleton
class FakeClassifierRepository @Inject constructor() : ClassifierPort{
    override suspend fun classify(image: Bitmap): Result<List<ClassifierEntity>> {
        // Simulamos una respuesta exitosa inmediata sin usar TFLite
        val fakeData = listOf(
            ClassifierEntity(
                label = "Betta",
                score = 0.99f
            ),
            ClassifierEntity(
                label = "Guppy",
                score = 0.23f
            ),
            ClassifierEntity(
                label = "Molly",
                score = 0.06f
            )
        )
        return Result.Success(fakeData)
    }
}