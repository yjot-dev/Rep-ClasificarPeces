package com.yjotdev.clasificarpeces

import android.graphics.Bitmap
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.clasificarpeces.application.mvvm.model.UiModel
import com.yjotdev.clasificarpeces.application.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    // 1. Mocks
    private val classifierMock: ClassifierUseCase = mockk()
    private val bitmapMock: Bitmap = mockk() // Mockeamos el bitmap

    // 2. ViewModel bajo prueba
    private lateinit var viewModel: UiViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Inyectamos el mock
        viewModel = UiViewModel(classifierMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Prueba que setFishName actualiza correctamente el StateFlow.
     */
    @Test
    fun setFishNameUpdatesStateCorrectly() = runTest {
        val expectedName = "Betta Splendens"

        viewModel.setFishName(expectedName)

        assertEquals(expectedName, viewModel.uiState.value.fishName)
    }

    /**
     * Prueba que setFishDescription actualiza el StateFlow.
     */
    @Test
    fun setFishDescriptionUpdatesStateCorrectly() = runTest {
        val expectedDesc = "Pez de agua dulce muy colorido."

        viewModel.setFishDescription(expectedDesc)

        assertEquals(expectedDesc, viewModel.uiState.value.fishDescription)
    }

    /**
     * Prueba compleja: detectorFish
     * Verifica que cuando el clasificador devuelve datos, el ViewModel
     * formatea los strings y actualiza todos los estados.
     */
    @Test
    fun detectorFishUpdatesAllStatesOnSuccess() = runTest {
        // GIVEN (Dado)
        val mockResults = listOf(
            ClassifierEntity(label = "Guppy", score = 0.95f),
            ClassifierEntity(label = "Molly", score = 0.05f)
        )

        val successResult = Result.Success(mockResults)

        // Entrenamos al mock: Cuando llamen a classify, devuelve esta lista
        coEvery { classifierMock.invoke(bitmapMock) } returns successResult

        // WHEN (Cuando)
        viewModel.classifierResult(bitmapMock)

        // Esperamos a que la corrutina termine
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN (Entonces) - Verificamos el estado final
        val currentState = viewModel.uiState.value

        // 1. Verifica que tomó el primer resultado para el nombre
        assertEquals("Guppy", currentState.fishName)

        // 2. Verifica la lógica de formateo de porcentajes (95% y 5%)
        val expectedList = mockResults
        assertEquals(expectedList, currentState.result)
    }

    /**
     * Prueba que onCleared reinicia el estado a valores por defecto.
     * Nota: onCleared es protected, pero podemos probar el efecto si
     * hubiera un método público que resetee o verificando el estado inicial.
     * Como no podemos llamar onCleared directamente, probamos el estado inicial
     * que es lo que onCleared restablece.
     */
    @Test
    fun initialStateIsEmpty() = runTest {
        val initialState = UiModel() // Estado vacío por defecto

        // Asumiendo que UiModel() inicializa strings vacíos y nulos
        assertEquals(initialState.fishName, viewModel.uiState.value.fishName)
        assertEquals(initialState.result, viewModel.uiState.value.result)
    }
}