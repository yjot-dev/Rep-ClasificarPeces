package com.yjotdev.clasificarpeces.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SpeciesDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("tipo") val type: String = "",
    @SerializedName("nombreComun") val commonName: String = "",
    @SerializedName("nombreCientifico") val scientificName: String = "",
    @SerializedName("habitatNatural") val naturalHabitat: String = "",
    @SerializedName("dieta") val diet: String = "",
    @SerializedName("ph") val ph: String = "",
    @SerializedName("temperatura") val temperature: String = "",
    @SerializedName("espacio") val space: String = "",
    @SerializedName("imagenUri") val imageUri: String = ""
)