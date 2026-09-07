package com.yjotdev.clasificarpeces.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yjotdev.clasificarpeces.data.local.entity.SpeciesEntity
import com.yjotdev.clasificarpeces.data.local.converter.SpeciesConverter
import com.yjotdev.clasificarpeces.data.local.dao.SpeciesDao

@Database(entities = [SpeciesEntity::class], version = 1, exportSchema = false)
@TypeConverters(SpeciesConverter::class)
abstract class SpeciesDatabase: RoomDatabase() {

    companion object {
        const val NAME = "bd_species"
    }

    abstract fun speciesDao(): SpeciesDao
}