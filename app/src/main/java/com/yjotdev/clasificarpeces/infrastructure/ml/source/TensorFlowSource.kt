package com.yjotdev.clasificarpeces.infrastructure.ml.source

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TensorFlowSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /** Ejecutar inferencia con el modelo de tensorflow **/
    fun runInference(image: Bitmap): MutableList<Category> {
        // Configurar opciones personalizadas
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(3)         // Mostrar hasta 3 clases
            .setScoreThreshold(0.0f)  // No descartar por score
            .build()
        // Cargar el modelo desde assets
        val classifier = ImageClassifier.createFromFileAndOptions(
            context, "modelo.tflite", options)
        // Convertir a TensorImage sin procesar manualmente
        val tensorImage = TensorImage.fromBitmap(image)
        // Ejecutar inferencia
        val results = classifier.classify(tensorImage)
        return results[0].categories
    }
}