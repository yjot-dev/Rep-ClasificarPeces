package com.yjotdev.clasificarpeces.application.mvvm.model

import android.graphics.Bitmap

data class UiModel(
    val fishName: String = "Guppy",
    val fishDescription: String = "Descripción:\nEl pez Guppy.",
    val fishImage: Bitmap? = null,
    val result: List<String>? = null
)