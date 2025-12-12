package com.yjotdev.clasificarpeces.application.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageProcessorHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /** Convertir uri a bitmap **/
    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            // Convertir URI a Bitmap de manera compatible con versiones nuevas y viejas
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true // Importante para TensorFlow si necesitas redimensionar luego
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}