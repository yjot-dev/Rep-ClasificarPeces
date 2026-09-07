package com.yjotdev.clasificarpeces.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

/**
 * TypeConverter para que Room pueda persistir una lista de SpeciesEntity.
 * Pertenece a la capa Data.
 */
@Suppress("unused")
class SpeciesConverter {
    @TypeConverter
    fun fromList(list: List<SpeciesModel>): String{
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String): List<SpeciesModel> {
        val listType = object : TypeToken<List<SpeciesModel>>() {}.type
        return Gson().fromJson(data, listType)
    }
}