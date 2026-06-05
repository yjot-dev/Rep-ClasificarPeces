package com.yjotdev.clasificarpeces.domain.repository

import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.model.ImageModel
import com.yjotdev.clasificarpeces.domain.core.Result

interface ClassifierRepository {
    suspend fun classifier(image: ImageModel): Result<List<ClassifierModel>>
}