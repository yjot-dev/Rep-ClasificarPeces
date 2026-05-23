package com.yjotdev.clasificarpeces.domain.repository

import android.graphics.Bitmap
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.core.Result

interface ClassifierRepository {
    suspend fun classify(image: Bitmap): Result<List<ClassifierModel>>
}