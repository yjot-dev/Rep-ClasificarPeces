package com.yjotdev.clasificarpeces

import app.cash.turbine.test
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesUseCase
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent

@OptIn(ExperimentalCoroutinesApi::class)
class UiViewModelTest {
    @RelaxedMockK
    private lateinit var getStringUseCase: GetStringUseCase

    @RelaxedMockK
    private lateinit var speciesUseCase: SpeciesUseCase

    private lateinit var viewModel: UiViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = UiViewModel(getStringUseCase, speciesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun whenFishSearchIsSuccessfulThenUiStateIsUpdated() = runTest {
        // Given
        val fakeSpeciesList = listOf(
            SpeciesModel(commonName = "Pez Dorado", scientificName = "Carassius auratus"),
            SpeciesModel(commonName = "Tiburón", scientificName = "Carcharodon carcharias")
        )
        coEvery { speciesUseCase(any(), any()) } returns Result.Success(fakeSpeciesList)

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                // Estado inicial
                assertEquals(null, awaitItem().fishResult)

                // Estado actualizado
                val updatedState = awaitItem()
                assertEquals(fakeSpeciesList, updatedState.fishResult)
            }
        }

        // When
        viewModel.fishSearch("pez")
        advanceUntilIdle()

        job1.cancel()
        coVerify(exactly = 1) { speciesUseCase(any(), any()) }
    }

    @Test
    fun whenFishSearchFailsThenUiStateIsResetAndEventsAreSent() = runTest {
        // Given
        val exception = Exception("Error al consultar especies")
        coEvery { speciesUseCase(any(), any()) } returns Result.Error(exception)
        coEvery { getStringUseCase(R.string.speciesview_toast_error) } returns "Error al cargar especies"

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                // Estado inicial
                assertEquals(null, awaitItem().fishResult)

                // Estado actualizado tras error
                val updatedState = awaitItem()
                assertEquals(null, updatedState.fishResult)
            }
        }

        val job2 = launch {
            viewModel.eventChannel.test {
                // Verifica que se envía el Toast
                val toastEvent = awaitItem()
                assertTrue(toastEvent is UiEvent.ShowToast)
                assertEquals("Error al cargar especies", (toastEvent as UiEvent.ShowToast).message)

                // Verifica que se envía el Log
                val logEvent = awaitItem()
                assertTrue(logEvent is UiEvent.ShowLog)
                assertEquals("Error al consultar especies", (logEvent as UiEvent.ShowLog).message)
            }
        }

        // When
        viewModel.fishSearch("pez")
        advanceUntilIdle()

        job1.cancel()
        job2.cancel()
        coVerify(exactly = 1) { speciesUseCase(any(), any()) }
        coVerify(exactly = 1) { getStringUseCase(R.string.speciesview_toast_error) }
    }

    @Test
    fun whenSetInfoIsInvokedThenUiStateIsUpdatedWithFishInfo() = runTest {
        // Given
        val fakeSpecies = SpeciesModel(commonName = "Pez Dorado", scientificName = "Carassius auratus")

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                // Estado inicial
                assertEquals(SpeciesModel(), awaitItem().fishInfo)

                // When
                viewModel.setInfo(fakeSpecies)
                advanceUntilIdle()

                // Estado actualizado
                val updatedState = awaitItem()
                assertEquals(fakeSpecies, updatedState.fishInfo)
            }
        }

        job1.cancel()
    }
}