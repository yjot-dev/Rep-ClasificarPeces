package com.yjotdev.clasificarpeces.domain.entity

data class ClassifierEntity(
    val index: Int = 0,
    val label: String = "",
    val score: Float = 0f
)