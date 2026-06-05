package com.yjotdev.clasificarpeces.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ClassifierDto(
    @SerializedName("label") val label: String = "",
    @SerializedName("score") val score: Float = 0f
)