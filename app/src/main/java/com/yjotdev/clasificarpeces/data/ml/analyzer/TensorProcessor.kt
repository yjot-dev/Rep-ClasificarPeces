package com.yjotdev.clasificarpeces.data.ml.analyzer

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.data.ml.dto.RecognitionDto
import com.yjotdev.clasificarpeces.data.ml.mapper.toDto

/**
 * Clase encargada del postprocesamiento de los resultados obtenidos del modelo de ML.
 */
@Singleton
class TensorProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Prepara el buffer de salida para almacenar las probabilidades del modelo.
     * @param interpreter El intérprete de TFLite del cual obtener la forma de salida.
     * @return Un TensorBuffer inicializado con el tamaño y tipo de dato correcto.
     */
    fun prepareOutputBuffer(interpreter: Interpreter): TensorBuffer {
        return TensorBuffer.createFixedSize(
            interpreter.getOutputTensor(0).shape(),
            interpreter.getOutputTensor(0).dataType()
        )
    }

    /**
     * Procesa el buffer de salida para obtener los resultados legibles (etiquetas y probabilidades).
     * @param outputBuffer El buffer con los resultados de la inferencia.
     * @param labelPath Ruta al archivo de etiquetas en los assets.
     * @return Lista de los 3 mejores resultados obtenidos en formato DTO.
     */
    fun processResults(outputBuffer: TensorBuffer, labelPath: String): List<RecognitionDto> {
        // 1. Carga de etiquetas desde assets
        val labels = FileUtil.loadLabels(context, labelPath)

        // 2. Mapeo de probabilidades a etiquetas usando TensorLabel
        val labeledProbability = TensorLabel(labels, outputBuffer).categoryList

        // 3. Conversión a DTO y selección de los mejores 3 resultados
        return labeledProbability
            .map { it.toDto() }
            .take(3)
    }
}