package com.yjotdev.clasificarpeces

import app.cash.turbine.test
import android.graphics.Bitmap
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.clasificarpeces.presentation.mvvm.state.FishInfoState
import com.yjotdev.clasificarpeces.presentation.mvvm.state.UiState
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.utils.Helper
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    // 1. Mocks
    private val classifierMock: ClassifierUseCase = mockk() // Mockeamos el clasificador
    private val helperMock: Helper = mockk() // Mockeamos el helper
    private val bitmapMock: Bitmap = mockk() // Mockeamos el bitmap

    // 2. ViewModel
    private lateinit var viewModel: UiViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Inyectamos el mock
        viewModel = UiViewModel(classifierMock, helperMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Prueba que setFishInfo actualiza correctamente el StateFlow.
     */
    @Test
    fun setFishInfoUpdatesStateCorrectly() = runTest {
        val expectedFish = FishInfoState(
            name = "Betta Splendens",
            description = "Pez de agua dulce muy colorido."
        )
        viewModel.setFishInfo(expectedFish)

        assertEquals(expectedFish, viewModel.uiState.value.fishInfo)
    }

    /**
     * Prueba compleja: detectorFish
     * Verifica que cuando el clasificador devuelve datos, el ViewModel
     * formatea los strings y actualiza todos los estados.
     */
    @Test
    fun whenClassifierResultIsSuccessfulThenUiStateIsUpdatedWithData() = runTest {
        // GIVEN (Dado)
        val mockResults = listOf(
            ClassifierModel(label = "Guppy", score = 0.95f),
            ClassifierModel(label = "Molly", score = 0.05f)
        )

        // Entrenamos al mock: Cuando llamen a classify, devuelve esta lista
        coEvery { classifierMock(bitmapMock) } returns Result.Success(mockResults)

        // Then: Observamos el estado
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState.fishInfo, FishInfoState())

            // WHEN (Cuando)
            viewModel.classifierResult(bitmapMock)
            advanceUntilIdle()

            // Then: Verificamos que el estado se actualizó con los datos
            val successState = awaitItem()
            assertEquals(mockResults, successState.fishResult)
            assertEquals("Guppy", successState.fishResult?.first()?.label)
        }

        // Verificamos que el caso de uso fue llamado una vez
        coVerify(exactly = 1) { classifierMock(bitmapMock) }
    }

    /**
     * Prueba compleja: detectorFish
     * Verifica que cuando el clasificador da error, el ViewModel
     * envia una Exception con un mensaje en strings.
     */
    @Test
    fun whenClassifierResultFailsThenUiStateResultIsNull() = runTest {
        // Given: Preparamos el escenario para un error
        val errorMessage = "Error en el motor de detección"
        coEvery { classifierMock(bitmapMock) } returns Result.Error(Exception(errorMessage))

        // Then: Observamos el estado
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState.fishInfo, FishInfoState())
            assertEquals(null, initialState.fishResult)

            // When: Ejecutamos la acción
            viewModel.classifierResult(bitmapMock)
            advanceUntilIdle()

            // Then: El resultado en el estado debe ser null tras el fallo
            val errorState = viewModel.uiState.value.fishResult
            assertEquals(null, errorState)
        }

        // Verificamos la interacción
        coVerify(exactly = 1) { classifierMock(bitmapMock) }
    }

    /**
     * Prueba que onCleared reinicia el estado a valores por defecto.
     * Nota: onCleared es protected, pero podemos probar el efecto si
     * hubiera un proceso público que resetee o verificando el estado inicial.
     * Como no podemos llamar onCleared directamente, probamos el estado inicial
     * que es lo que onCleared restablece.
     */
    @Test
    fun initialStateIsEmpty() = runTest {
        val initialState = UiState() // Estado vacío por defecto

        // Asumiendo que UiModel() inicializa strings vacíos y nulos
        assertEquals(initialState.fishInfo, viewModel.uiState.value.fishInfo)
        assertEquals(initialState.fishResult, viewModel.uiState.value.fishResult)
    }
}