package com.yjotdev.clasificarpeces.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.yjotdev.clasificarpeces.data.local.entity.SpeciesEntity

/**
 * Interfaz DAO (Data Access Object) para Room.
 * Esta es la implementación concreta del puerto del dominio usando la tecnología Room.
 * Pertenece a la capa de Infraestructura.
 */
@Dao
interface SpeciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecies(items: List<SpeciesEntity>)

    @Query("SELECT * FROM species")
    fun getAllSpecies(): Flow<List<SpeciesEntity>>

    @Query("SELECT * FROM species WHERE " +
            "scientificName LIKE '%' || :query || '%' OR " +
            "commonName LIKE '%' || :query || '%' OR " +
            "type LIKE '%' || :query || '%'")
    fun searchBy(query: String): Flow<List<SpeciesEntity>>
}