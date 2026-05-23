package com.yjotdev.clasificarpeces.data.ml.mapper

import org.tensorflow.lite.support.label.Category
import com.yjotdev.clasificarpeces.data.ml.dto.RecognitionDto
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

fun RecognitionDto.toDomain() = ClassifierModel(
    index = this.index,
    label = this.label,
    score = this.score
)

fun Category.toDto() = RecognitionDto(
    index = this.index,
    label = this.label,
    score = this.score
)