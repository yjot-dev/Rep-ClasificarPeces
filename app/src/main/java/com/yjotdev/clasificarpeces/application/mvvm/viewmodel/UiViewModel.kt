package com.yjotdev.clasificarpeces.application.mvvm.viewmodel

import android.graphics.Bitmap
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.yjotdev.clasificarpeces.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.support.label.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yjotdev.clasificarpeces.application.mvvm.model.UiModel

@HiltViewModel
class UiViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(UiModel())
    val uiState: StateFlow<UiModel> = _uiState.asStateFlow()

    override fun onCleared() {
        _uiState.value = UiModel()
    }

    /** Estado del nombre del pez **/
    fun setFishName(value: String){
        _uiState.update { it.copy(fishName = value) }
    }

    /** Estado de la descripción del pez **/
    fun setFishDescription(value: String){
        _uiState.update { it.copy(fishDescription = value) }
    }

    /** Estado de la imagen del pez **/
    fun setFishImage(value: Bitmap?){
        _uiState.update { it.copy(fishImage = value) }
    }

    /** Estado de los resultados de la detección **/
    fun setResult(value: List<String>?){
        _uiState.update { it.copy(result = value) }
    }

    /** Convertir uri a bitmap **/
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
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

    /** Detectar raza de pez **/
    fun detectorFish(context: Context, image: Bitmap){
        val labels = context.resources.getStringArray(R.array.infoview_name)
        val descriptions = context.resources.getStringArray(R.array.infoview_description)
        //Detectar imagen con modelo de tensorflow
        val result = runInference(context, image)
        //Formato del resultado
        val items = MutableList(result.size) {""}
        result.forEach { item ->
            val percent = (item.score * 100)
            val i = item.index
            items[i] = "${labels[i]} - ${percent.roundToInt()}%"
        }
        //Guarda datos en el viewModel
        val i = result[0].index
        setFishName(labels[i])
        setFishDescription(descriptions[i])
        setResult(items)
    }

    /** Ejecutar inferencia con el modelo de tensorflow **/
    private fun runInference(context: Context, image: Bitmap): MutableList<Category> {
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