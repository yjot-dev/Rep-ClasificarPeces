package com.yjotdev.clasificarpeces.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.core.mapSuccess
import com.yjotdev.clasificarpeces.data.remote.api.ClassifierApi
import com.yjotdev.clasificarpeces.data.remote.core.safeApiCallForBody
import com.yjotdev.clasificarpeces.data.remote.mapper.toDomain
import com.yjotdev.clasificarpeces.data.remote.mapper.toDto
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.model.ImageModel

@Singleton
class ClassifierRepositoryImpl @Inject constructor(
    private val classifierApi: ClassifierApi
) : ClassifierRepository {

    override suspend fun classifier(image: ImageModel): Result<List<ClassifierModel>> {
        return safeApiCallForBody { classifierApi.classifier(image.toDto()) }
            .mapSuccess { result -> result.map { it.toDomain() } }
    }
}