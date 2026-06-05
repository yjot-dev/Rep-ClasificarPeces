package com.yjotdev.clasificarpeces.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

/**
 * Convierte una Uri a Bitmap.
 * @param context El contexto de la aplicación o actividad.
 * @return El Bitmap decodificado o null si ocurre un error.
 */
fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/**
 * Convierte un objeto Bitmap a una cadena en formato Base64.
 * @param quality Calidad de la compresión (1-100).
 * @return String codificado en Base64.
 */
fun Bitmap.toBase64(quality: Int = 100): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray,Base64.NO_WRAP)
}

class ImageProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun uriToBitmap(uri: Uri): Bitmap? = uri.toBitmap(context)
}