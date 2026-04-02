package com.yjotdev.clasificarpeces.application.mvvm.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yjotdev.clasificarpeces.application.mvvm.model.FishInfoModel
import com.yjotdev.clasificarpeces.application.mvvm.model.ImageInput
import com.yjotdev.clasificarpeces.application.mvvm.model.UiModel
import com.yjotdev.clasificarpeces.application.utils.Helper
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase

@HiltViewModel
class UiViewModel @Inject constructor(
    private val classifierUseCase: ClassifierUseCase,
    private val helper: Helper
): ViewModel() {
    private val _uiState = MutableStateFlow(UiModel())
    val uiState: StateFlow<UiModel> = _uiState.asStateFlow()

    override fun onCleared() {
        _uiState.value = UiModel()
    }

    /** Estado del nombre del pez **/
    fun setFishInfo(value: FishInfoModel){
        _uiState.update { it.copy(fishInfo = value) }
    }

    /** Estado de la imagen del pez **/
    fun setFishImage(input: ImageInput){
        val bitmap = when(input) {
            is ImageInput.FromBitmap -> input.bitmap
            is ImageInput.FromUri -> helper.uriToBitmap(input.uri)
        }
        _uiState.update { it.copy(fishImage = bitmap) }
    }

    /** Estado de los resultados de la detección **/
    fun classifierResult(image: Bitmap){
        viewModelScope.launch {
            when (val result = classifierUseCase(image)) {
                is Result.Success -> {
                    _uiState.update { it.copy(fishResult = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(fishResult = null) }
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
            val value = FishInfoModel(
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