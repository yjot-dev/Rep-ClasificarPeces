package com.yjotdev.clasificarpeces.presentation.mvvm.state

import android.graphics.Bitmap
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

data class UiState(
    val fishInfo: FishInfoState = FishInfoState(),
    val fishImage: Bitmap? = null,
    val fishResult: List<ClassifierModel>? = null
)