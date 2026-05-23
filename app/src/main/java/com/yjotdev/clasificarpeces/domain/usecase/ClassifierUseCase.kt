package com.yjotdev.clasificarpeces.domain.usecase

import android.graphics.Bitmap
import javax.inject.Inject
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

class ClassifierUseCase @Inject constructor(
    private val classifierRepository: ClassifierRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): Result<List<ClassifierModel>> {
        return classifierRepository.classify(bitmap)
    }
}