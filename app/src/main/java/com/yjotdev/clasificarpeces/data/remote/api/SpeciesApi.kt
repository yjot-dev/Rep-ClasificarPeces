package com.yjotdev.clasificarpeces.data.remote.api

import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.GET
import com.yjotdev.clasificarpeces.data.remote.dto.SpeciesDto

/**
 * Interfaz de Retrofit para la clasificacion de imagenes de la API.
 * ESTA interfaz pertenece a la capa de Infraestructura y define los endpoints HTTP.
 */
interface SpeciesApi {
    @GET("species")
    suspend fun seleccionarEspecies(
        @Query("searchedText") searchedText: String? = null,
        @Query("language") language: String
    ): Response<List<SpeciesDto>>
}