package com.yjotdev.clasificarpeces.application.mvvm.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yjotdev.clasificarpeces.application.mvvm.model.UiModel
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase

@HiltViewModel
class UiViewModel @Inject constructor(
    private val classifierUseCase: ClassifierUseCase
): ViewModel() {
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
    fun classifierResult(image: Bitmap){
        viewModelScope.launch {
            when (val result = classifierUseCase(image)) {
                is Result.Success -> {
                    _uiState.update { it.copy(result = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(result = null) }
                }
            }
        }
    }
}