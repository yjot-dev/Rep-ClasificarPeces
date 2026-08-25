package com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yjotdev.clasificarpeces.presentation.mvvm.state.UiState
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent
import com.yjotdev.clasificarpeces.presentation.utils.Helper
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesUseCase
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.R

@HiltViewModel
class UiViewModel @Inject constructor(
    private val getStringUseCase: GetStringUseCase,
    private val speciesUseCase: SpeciesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    private val _eventChannel = Channel<UiEvent>()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val eventChannel = _eventChannel.receiveAsFlow()

    override fun onCleared() {
        _uiState.value = UiState()
    }

    /** Estado que muestra la información de un pez seleccionado **/
    fun setInfo(value: SpeciesModel){
        _uiState.update { it.copy(fishInfo = value)}
    }

    /**
     * Busca una lista de peces según el texto ingresado
     * caso contrario devuelve la lista completa.
     **/
    fun fishSearch(searchedText: String? = null) {
        viewModelScope.launch {
            val language = Helper.getDeviceLanguage()
            when(val result = speciesUseCase(searchedText, language)) {
                is Result.Success -> {
                    _uiState.update { it.copy(fishResult = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(fishResult = null) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getStringUseCase(R.string.speciesview_toast_error)
                    ))
                    _eventChannel.send(UiEvent.ShowLog(
                        result.exception.message!!
                    ))
                }
            }
        }
    }
}