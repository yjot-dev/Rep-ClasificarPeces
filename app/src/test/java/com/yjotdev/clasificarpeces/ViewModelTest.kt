package com.yjotdev.clasificarpeces

import app.cash.turbine.test
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlinx.coroutines.flow.flowOf
import org.junit.*
import org.junit.Assert.*
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesApiUseCase
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesDaoUseCase
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent

@OptIn(ExperimentalCoroutinesApi::class)
class UiViewModelTest {
    @RelaxedMockK
    private lateinit var getStringUseCase: GetStringUseCase

    @RelaxedMockK
    private lateinit var speciesApiUseCase: SpeciesApiUseCase

    @RelaxedMockK
    private lateinit var speciesDaoUseCase: SpeciesDaoUseCase

    private lateinit var viewModel: UiViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        every { speciesDaoUseCase() } returns flowOf(listOf(SpeciesModel(id = 1)))
        viewModel = UiViewModel(getStringUseCase, speciesApiUseCase, speciesDaoUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun whenSetInfoIsInvokedThenUiStateIsUpdatedWithSpeciesInfo() = runTest {
        // Given
        val fakeSpecies = SpeciesModel(commonName = "Pez Payaso", scientificName = "Amphiprioninae")

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                // Estado inicial
                assertEquals(SpeciesModel(), awaitItem().fishInfo)

                // When
                viewModel.setInfo(fakeSpecies)

                // Estado actualizado
                assertEquals(fakeSpecies, awaitItem().fishInfo)
            }
        }

        advanceUntilIdle()
        job1.cancel()
    }

    @Test
    fun whenGetRemoteDataIsSuccessfulThenDaoUseCaseIsInvokedToInsertData() = runTest {
        // Given
        val fakeSpeciesList = listOf(
            SpeciesModel(id = 10, commonName = "Betta", scientificName = "Betta splendens")
        )
        coEvery { speciesApiUseCase(any()) } returns Result.Success(fakeSpeciesList)

        // When
        viewModel.getRemoteData()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { speciesDaoUseCase(fakeSpeciesList) }
    }

    @Test
    fun whenGetRemoteDataFailsThenUiStateIsUpdatedAndEventsAreSent() = runTest {
        // Given
        val errorMessage = "Error al obtener datos"
        val exception = Exception(errorMessage)
        val toastText = "Error de conexión"
        coEvery { speciesApiUseCase(any()) } returns Result.Error(exception)
        coEvery { getStringUseCase(R.string.speciesview_toast_error) } returns toastText

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                skipItems(1)
                val state = awaitItem()
                assertEquals(emptyList<SpeciesModel>(), state.fishResult)
            }
        }

        val job2 = launch {
            viewModel.eventChannel.test {
                assertEquals(UiEvent.ShowToast(toastText), awaitItem())
                val logEvent = awaitItem() as UiEvent.ShowLog
                assertEquals(errorMessage, logEvent.message)
            }
        }

        // When
        viewModel.getRemoteData()
        advanceUntilIdle()

        // Final verification
        job1.cancel()
        job2.cancel()
        coVerify(exactly = 1) { speciesApiUseCase(any()) }
    }

    @Test
    fun whenSearchLocalDataIsInvokedThenUiStateIsUpdatedWithFilteredList() = runTest {
        // Given
        val query = "Goldfish"
        val filteredList = listOf(
            SpeciesModel(commonName = "Goldfish", scientificName = "Carassius auratus")
        )
        every { speciesDaoUseCase(query) } returns flowOf(filteredList)

        // Then
        val job1 = launch {
            viewModel.uiState.test {
                skipItems(1) // Salta el estado inicial del init

                // When
                viewModel.searchLocalData(query)

                // Estado con los resultados de búsqueda
                assertEquals(filteredList, awaitItem().fishResult)
            }
        }

        advanceUntilIdle()
        job1.cancel()
        coVerify(exactly = 1) { speciesDaoUseCase(query) }
    }
}