package com.yjotdev.clasificarpeces.domain.usecase

import javax.inject.Inject
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.model.ImageModel

class ClassifierUseCase @Inject constructor(
    private val classifierRepository: ClassifierRepository
) {
    suspend operator fun invoke(image: ImageModel): Result<List<ClassifierModel>> {
        return classifierRepository.classifier(image)
    }
}