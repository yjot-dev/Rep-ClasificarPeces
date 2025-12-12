package com.yjotdev.clasificarpeces.application.mvvm.model

import android.graphics.Bitmap
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity

data class UiModel(
    val fishName: String = "",
    val fishDescription: String = "",
    val fishImage: Bitmap? = null,
    val result: List<ClassifierEntity>? = null
)