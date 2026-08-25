package com.yjotdev.clasificarpeces.data.remote.mapper

import com.yjotdev.clasificarpeces.data.remote.dto.SpeciesDto
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

fun SpeciesDto.toDomain() = SpeciesModel(
    id = this.id,
    type = this.type,
    commonName = this.commonName,
    scientificName = this.scientificName,
    naturalHabitat = this.naturalHabitat,
    diet = this.diet,
    ph = this.ph,
    temperature = this.temperature,
    space = this.space,
    imageUri = this.imageUri
)

fun SpeciesModel.toDto() = SpeciesDto(
    id = this.id,
    type = this.type,
    commonName = this.commonName,
    scientificName = this.scientificName,
    naturalHabitat = this.naturalHabitat,
    diet = this.diet,
    ph = this.ph,
    temperature = this.temperature,
    space = this.space,
    imageUri = this.imageUri
)