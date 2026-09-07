package com.yjotdev.clasificarpeces.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "species")
data class SpeciesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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
