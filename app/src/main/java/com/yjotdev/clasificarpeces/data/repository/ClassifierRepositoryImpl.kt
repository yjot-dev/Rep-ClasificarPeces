package com.yjotdev.clasificarpeces.data.repository

import android.graphics.Bitmap
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.data.ml.datasource.TfLiteDataSource
import com.yjotdev.clasificarpeces.data.ml.mapper.toDomain
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

@Singleton
class ClassifierRepositoryImpl @Inject constructor(
    private val tfLiteDataSource: TfLiteDataSource
) : ClassifierRepository{

    override suspend fun classify(image: Bitmap): Result<List<ClassifierModel>> {
        return try {
            // Ejecutamos la inferencia
            val listDto = tfLiteDataSource.runInference(image)
            // Si tiene éxito, mapeamos a dominio y envolvemos en Success
            Result.Success(listDto.map { it.toDomain() })
        } catch(e: Exception) {
            // Si algo falla, capturamos el mensaje y envolvemos en Error
            Log.d("TfLite","Error durante la clasificación: $e")
            Result.Error(e)
        }
    }
}