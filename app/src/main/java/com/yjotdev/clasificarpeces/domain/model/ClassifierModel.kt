package com.yjotdev.clasificarpeces.domain.model

data class ClassifierModel(
    val index: Int = 0,
    val label: String = "",
    val score: Float = 0f
)