package com.yjotdev.clasificarpeces.application.mvvm.model

import android.graphics.Bitmap
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity

data class UiModel(
    val fishInfo: FishInfoModel = FishInfoModel(),
    val fishImage: Bitmap? = null,
    val fishResult: List<ClassifierEntity>? = null
)