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
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesApiUseCase
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesDaoUseCase
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.R

@HiltViewModel
class UiViewModel @Inject constructor(
    private val getStringUseCase: GetStringUseCase,
    private val speciesApiUseCase: SpeciesApiUseCase,
    private val speciesDaoUseCase: SpeciesDaoUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    private val _eventChannel = Channel<UiEvent>()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        observeSpecies()
    }

    override fun onCleared() {
        _uiState.value = UiState()
    }

    /** Estado que muestra la información de una especie marina seleccionada **/
    fun setInfo(value: SpeciesModel){
        _uiState.update { it.copy(fishInfo = value)}
    }

    /** Obtiene la lista completa de especies marinas desde la base de datos remota **/
    fun getRemoteData() {
        viewModelScope.launch {
            val language = Helper.getDeviceLanguage()
            when(val result = speciesApiUseCase(language)) {
                is Result.Success -> {
                    speciesDaoUseCase(result.data)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(fishResult = emptyList()) }
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

    /** Busca especies marinas desde la base de datos local **/
    fun searchLocalData(query: String) {
        viewModelScope.launch {
            speciesDaoUseCase(query).collect { list ->
                _uiState.update { it.copy(fishResult = list) }
            }
        }
    }

    private fun observeSpecies() {
        viewModelScope.launch {
            speciesDaoUseCase().collect { list ->
                _uiState.update { it.copy(fishResult = list) }
                if(list.isEmpty()){
                    getRemoteData()
                }
            }
        }
    }
}