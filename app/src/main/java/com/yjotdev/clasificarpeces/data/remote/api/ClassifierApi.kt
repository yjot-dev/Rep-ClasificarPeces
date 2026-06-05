package com.yjotdev.clasificarpeces.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.yjotdev.clasificarpeces.data.remote.dto.ClassifierDto
import com.yjotdev.clasificarpeces.data.remote.dto.ImageDto

/**
 * Interfaz de Retrofit para la clasificacion de imagenes de la API.
 * ESTA interfaz pertenece a la capa de Infraestructura y define los endpoints HTTP.
 */
interface ClassifierApi {
    @POST("classify")
    suspend fun classifier(@Body image: ImageDto): Response<List<ClassifierDto>>
}