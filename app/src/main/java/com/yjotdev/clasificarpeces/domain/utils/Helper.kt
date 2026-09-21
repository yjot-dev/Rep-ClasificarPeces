package com.yjotdev.clasificarpeces.domain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import java.io.File
import java.util.Locale

object Helper {
    fun getDeviceLanguage(): String = Locale.getDefault().language

    suspend fun saveImageLocally(context: Context, uri: String, fileName: String): String {
        if (uri.isBlank()) return "" // Devuelve una cadena vacía

        return try {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .allowHardware(false)
                .build()

            val drawable = (context.imageLoader.execute(request).drawable as BitmapDrawable).bitmap
            val file = File(context.filesDir, "$fileName.png")
            file.outputStream().use { out ->
                drawable.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            file.absolutePath // Ruta local que se guardará en Room
        } catch(e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}