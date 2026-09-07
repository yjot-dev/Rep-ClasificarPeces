package com.yjotdev.clasificarpeces.presentation.mvvm.state

import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

data class UiState(
    val fishInfo: SpeciesModel = SpeciesModel(),
    val fishResult: List<SpeciesModel> = emptyList()
)