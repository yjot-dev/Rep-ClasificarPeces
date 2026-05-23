package com.yjotdev.clasificarpeces.data.ml.dto

data class RecognitionDto(
    val index: Int = 0,
    val label: String = "",
    val score: Float = 0f
)