package com.yjotdev.clasificarpeces.infrastructure.repository

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton
import org.tensorflow.lite.support.label.Category
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity
import com.yjotdev.clasificarpeces.domain.port.ClassifierPort
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.infrastructure.ml.source.TensorFlowSource

@Singleton
class ClassifierRepository @Inject constructor(
    private val tensorflowSource: TensorFlowSource
) : ClassifierPort{
    override suspend fun classify(image: Bitmap): Result<List<ClassifierEntity>> {
        val result = tensorflowSource.runInference(image)
        return mapToEntity(result)
    }

    private fun mapToEntity(result: List<Category>): Result<List<ClassifierEntity>> {
        val list = MutableList(result.size) {ClassifierEntity()}
        result.forEach { item ->
            val i = result.indexOf(item)
            val data = ClassifierEntity(
                label = result[i].label,
                score = result[i].score
            )
            list[i] = data
        }
        return if(list.isNotEmpty())
            Result.Success(list)
        else
            Result.Error(Exception("Pez no detectado"))
    }
}