package com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.roundToInt
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yjotdev.clasificarpeces.presentation.mvvm.state.FishInfoState
import com.yjotdev.clasificarpeces.presentation.mvvm.state.UiState
import com.yjotdev.clasificarpeces.presentation.utils.ImageInput
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent
import com.yjotdev.clasificarpeces.presentation.utils.toBase64
import com.yjotdev.clasificarpeces.presentation.utils.ImageProvider
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.R
import com.yjotdev.clasificarpeces.domain.model.ImageModel

@HiltViewModel
class UiViewModel @Inject constructor(
    private val getStringUseCase: GetStringUseCase,
    private val classifierUseCase: ClassifierUseCase,
    private val imageProvider: ImageProvider
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    private val _eventChannel = Channel<UiEvent>()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val eventChannel = _eventChannel.receiveAsFlow()

    override fun onCleared() {
        _uiState.value = UiState()
    }

    /** Estado del nombre del pez **/
    fun setFishInfo(value: FishInfoState){
        _uiState.update { it.copy(fishInfo = value) }
    }

    /** Estado de la imagen del pez **/
    fun setFishImage(input: ImageInput){
        val bitmap = when(input) {
            is ImageInput.FromBitmap -> input.bitmap
            is ImageInput.FromUri -> imageProvider.uriToBitmap(input.uri)
        }
        _uiState.update { it.copy(fishImage = bitmap) }
    }

    /** Estado de los resultados de la detección **/
    fun classifierResult(image: Bitmap){
        viewModelScope.launch {
            val imageModel = ImageModel("data:image/png;base64,${image.toBase64()}")
            when (val result = classifierUseCase(imageModel)) {
                is Result.Success -> {
                    _uiState.update { it.copy(fishResult = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(fishResult = null) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getStringUseCase(R.string.toast_classifier_error)
                    ))
                    _eventChannel.send(UiEvent.ShowLog(
                        result.exception.message!!
                    ))
                }
            }
        }
    }

    /** Muestra la informacion completa de la clasificacion mas alta **/
    fun showInfo(
        labels: Array<String>,
        descriptions: Array<String>
    ){
        val stateList = _uiState.value.fishResult
        if (!stateList.isNullOrEmpty()) {
            val bestItem = stateList.maxBy { it.score }
            val index = stateList.indexOf(bestItem)
            val value = FishInfoState(
                name = labels[index],
                description = descriptions[index]
            )
            setFishInfo(value)
        }
    }

    /** Obtiene la lista traducida al idioma del dispositivo **/
    fun getTranslatedList(labels: Array<String>) : List<String> {
        val stateList = _uiState.value.fishResult
        val listIn = labels.toList()
        val listOut = MutableList(listIn.size){""}
        stateList?.forEach { item ->
            val index = stateList.indexOf(item)
            val percent = (item.score * 100).roundToInt()
            listOut[index] = "${listIn[index]} - $percent%"
        } ?: run {
            listIn.forEach { item ->
                val index = listIn.indexOf(item)
                listOut[index] = "$item - 0%"
            }
        }
        return listOut
    }
}