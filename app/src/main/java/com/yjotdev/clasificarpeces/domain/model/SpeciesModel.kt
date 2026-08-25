package com.yjotdev.clasificarpeces.domain.model

data class SpeciesModel(
    val id: Int = 0,
    val type: String = "",
    val commonName: String = "",
    val scientificName: String = "",
    val naturalHabitat: String = "",
    val diet: String = "",
    val ph: String = "",
    val temperature: String = "",
    val space: String = "",
    val imageUri: String = ""
)