package com.yjotdev.clasificarpeces.infrastructure.ml.source

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TensorFlowSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interpreter: Interpreter? = null
    private val modelPath = "modelo.tflite"
    private val labelPath = "labels.txt"

    /**
     * Ejecuta la inferencia sobre una imagen Bitmap.
     * @param image Imagen a clasificar.
     * @return Lista de categorías ordenadas por probabilidad.
     */
    fun runInference(image: Bitmap): List<Category> {
        // Inicialización del intérprete usando FileUtil de TF Support
        if (interpreter == null) {
            val model = FileUtil.loadMappedFile(context, modelPath)
            interpreter = Interpreter(model)
        }

        val currentInterpreter = interpreter!!

        // 1. Configuración de dimensiones
        val inputShape = currentInterpreter.getInputTensor(0).shape() // [1, height, width, 3]
        val height = inputShape[1]
        val width = inputShape[2]

        // 2. Preprocesamiento de imagen
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        var tensorImage = TensorImage(currentInterpreter.getInputTensor(0).dataType())
        tensorImage.load(image)
        tensorImage = imageProcessor.process(tensorImage)

        // 3. Preparación del buffer de salida
        val outputProbabilityBuffer = TensorBuffer.createFixedSize(
            currentInterpreter.getOutputTensor(0).shape(),
            currentInterpreter.getOutputTensor(0).dataType()
        )

        // 4. Inferencia
        currentInterpreter.run(tensorImage.buffer, outputProbabilityBuffer.buffer.rewind())

        // 5. Postprocesamiento: Mapeo de etiquetas
        // Usamos FileUtil para cargar las etiquetas desde assets
        val labels = FileUtil.loadLabels(context, labelPath)
        val labeledProbability = TensorLabel(labels, outputProbabilityBuffer).categoryList

        // Retornar los mejores 3 resultados
        return labeledProbability.take(3)
    }
}