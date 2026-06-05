package com.yjotdev.clasificarpeces.data.remote.mapper

import com.yjotdev.clasificarpeces.data.remote.dto.ClassifierDto
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

fun ClassifierDto.toDomain() = ClassifierModel(
    label = this.label,
    score = this.score
)

fun ClassifierModel.toDto() = ClassifierDto(
    label = this.label,
    score = this.score
)