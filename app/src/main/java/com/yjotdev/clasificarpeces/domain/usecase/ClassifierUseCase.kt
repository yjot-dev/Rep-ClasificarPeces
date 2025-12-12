package com.yjotdev.clasificarpeces.domain.usecase

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.port.ClassifierPort
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity

@Singleton
class ClassifierUseCase @Inject constructor(
    private val classifierPort: ClassifierPort
) {
    suspend operator fun invoke(bitmap: Bitmap): Result<List<ClassifierEntity>> {
        return classifierPort.classify(bitmap)
    }
}