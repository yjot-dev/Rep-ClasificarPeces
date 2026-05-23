package com.yjotdev.clasificarpeces.data.ml.datasource

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.data.ml.analyzer.ImageAnalyzer
import com.yjotdev.clasificarpeces.data.ml.analyzer.TensorProcessor
import com.yjotdev.clasificarpeces.data.ml.dto.RecognitionDto

@Singleton
class TfLiteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageAnalyzer: ImageAnalyzer,
    private val tensorProcessor: TensorProcessor
) {
    private var interpreter: Interpreter? = null
    private val modelPath = "modelo.tflite"
    private val labelPath = "labels.txt"

    /**
     * Coordina el proceso de inferencia: preprocesamiento, ejecución y postprocesamiento.
     * @param image Imagen a clasificar.
     * @return Lista de los 3 mejores resultados obtenidos.
     */
    fun runInference(image: Bitmap): List<RecognitionDto> {
        // 1. Inicialización diferida del intérprete
        if (interpreter == null) {
            val model = FileUtil.loadMappedFile(context, modelPath)
            interpreter = Interpreter(model)
        }

        val currentInterpreter = interpreter!!

        // 2. Pre-procesamiento de la imagen (Delegado a ImageAnalyzer)
        val tensorImage = imageAnalyzer.processImage(image, currentInterpreter)

        // 3. Preparación del buffer de salida (Delegado a TensorProcessor)
        val outputBuffer = tensorProcessor.prepareOutputBuffer(currentInterpreter)

        // 4. Ejecución de la inferencia
        currentInterpreter.run(tensorImage.buffer, outputBuffer.buffer.rewind())

        // 5. Post-procesamiento de resultados (Delegado a TensorProcessor)
        return tensorProcessor.processResults(outputBuffer, labelPath)
    }
}