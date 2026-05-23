package com.yjotdev.clasificarpeces.data.ml.analyzer

import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encargada del preprocesamiento de imágenes para su uso en el modelo de ML.
 */
@Singleton
class ImageAnalyzer @Inject constructor() {
    /**
     * Procesa una imagen Bitmap y la convierte en un TensorImage adecuado para el modelo.
     * @param image La imagen Bitmap a clasificar.
     * @param interpreter El intérprete de TFLite para obtener la configuración de entrada.
     * @return El objeto TensorImage procesado y listo para la inferencia.
     */
    fun processImage(image: Bitmap, interpreter: Interpreter): TensorImage {
        // 1. Configuración de dimensiones dinámicas basadas en el modelo
        val inputShape = interpreter.getInputTensor(0).shape() // [1, height, width, 3]
        val height = inputShape[1]
        val width = inputShape[2]

        // 2. Definición del procesador de imagen (Resizing)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        // 3. Carga y procesamiento de la imagen
        var tensorImage = TensorImage(interpreter.getInputTensor(0).dataType())
        tensorImage.load(image)
        tensorImage = imageProcessor.process(tensorImage)

        return tensorImage
    }
}