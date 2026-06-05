package com.yjotdev.clasificarpeces.data.remote.mapper

import com.yjotdev.clasificarpeces.data.remote.dto.ImageDto
import com.yjotdev.clasificarpeces.domain.model.ImageModel

fun ImageDto.toDomain() = ImageModel(
    image = this.image
)

fun ImageModel.toDto() = ImageDto(
    image = this.image
)