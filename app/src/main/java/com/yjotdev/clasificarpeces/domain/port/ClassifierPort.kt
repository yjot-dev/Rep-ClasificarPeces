package com.yjotdev.clasificarpeces.domain.port

import android.graphics.Bitmap
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity
import com.yjotdev.clasificarpeces.domain.core.Result

interface ClassifierPort {
    suspend fun classify(image: Bitmap): Result<List<ClassifierEntity>>
}