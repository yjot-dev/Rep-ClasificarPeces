package com.yjotdev.clasificarpeces.data.local.mapper

import com.yjotdev.clasificarpeces.data.local.entity.SpeciesEntity
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

fun SpeciesEntity.toDomain() = SpeciesModel(
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

fun SpeciesModel.toBD() = SpeciesEntity(
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