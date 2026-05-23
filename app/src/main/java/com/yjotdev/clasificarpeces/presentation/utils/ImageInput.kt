package com.yjotdev.clasificarpeces.presentation.utils

import android.graphics.Bitmap
import android.net.Uri

/**
 * Representa los diferentes tipos de entrada de imagen permitidos en la aplicación.
 */
sealed class ImageInput {
    /** Representa una imagen proveniente de un objeto Bitmap directo (ej. Cámara) */
    data class FromBitmap(val bitmap: Bitmap) : ImageInput()
    /** Representa una imagen proveniente de una Uri (ej. Galería) */
    data class FromUri(val uri: Uri) : ImageInput()
}