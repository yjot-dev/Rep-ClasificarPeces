package com.yjotdev.clasificarpeces

import android.graphics.Bitmap
import app.cash.turbine.test
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.model.ClassifierModel
import com.yjotdev.clasificarpeces.domain.model.ImageModel
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase
import com.yjotdev.clasificarpeces.domain.usecase.GetStringUseCase
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent
import com.yjotdev.clasificarpeces.presentation.utils.ImageProvider
import com.yjotdev.clasificarpeces.presentation.utils.toBase64

@OptIn(ExperimentalCoroutinesApi::class)
class UiViewModelTest {

    @MockK
    lateinit var getStringUseCase: GetStringUseCase

    @MockK
    lateinit var classifierUseCase: ClassifierUseCase

    @MockK
    lateinit var imageProvider: ImageProvider

    @MockK
    lateinit var bitmapMock: Bitmap

    private lateinit var viewModel: UiViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // Inicializamos el ViewModel con sus dependencias mockeadas
        viewModel = UiViewModel(getStringUseCase, classifierUseCase, imageProvider)

        // Mockeamos la clase que contiene la función de extensión toBase64
        mockkStatic("com.yjotdev.clasificarpeces.presentation.utils.HelperKt")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic("com.yjotdev.clasificarpeces.presentation.utils.HelperKt")
        clearAllMocks()
    }

    @Test
    fun `when classifierResult is called and returns success then update fishResult state`() = runTest {
        // GIVEN
        val fakeBase64 = "base64EncodedString"
        val expectedApiString = ImageModel("data:image/jpeg;base64,$fakeBase64")
        val expectedResults = listOf(
            ClassifierModel(label = "Trucha", score = 0.95f),
            ClassifierModel(label = "Salmon", score = 0.05f)
        )

        every { bitmapMock.toBase64() } returns fakeBase64
        coEvery { classifierUseCase(expectedApiString) } returns Result.Success(expectedResults)

        // WHEN & THEN
        viewModel.uiState.test {
            // Estado inicial
            assertEquals(null, awaitItem().fishResult)

            viewModel.classifierResult(bitmapMock)
            advanceUntilIdle()

            // Verificamos que el estado se actualizó con los resultados
            val updatedState = awaitItem()
            assertEquals(expectedResults, updatedState.fishResult)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { classifierUseCase(expectedApiString) }
    }

    @Test
    fun `when classifierResult is called and returns error then set fishResult null and send events`() = runTest {
        // GIVEN
        val fakeBase64 = "base64ErrorString"
        val expectedApiString = ImageModel("data:image/jpeg;base64,$fakeBase64")
        val exceptionMessage = "Network Timeout"
        val exception = Exception(exceptionMessage)
        val toastErrorMessage = "Error al clasificar"

        every { bitmapMock.toBase64() } returns fakeBase64
        coEvery { classifierUseCase(expectedApiString) } returns Result.Error(exception)
        every { getStringUseCase(R.string.toast_classifier_error) } returns toastErrorMessage

        // WHEN & THEN (Probamos el Channel de eventos)
        viewModel.eventChannel.test {
            viewModel.classifierResult(bitmapMock)
            advanceUntilIdle()

            // Verificamos que se emitieron los eventos en el orden correcto
            assert(UiEvent.ShowToast(toastErrorMessage).message.isNotEmpty())
            assert(UiEvent.ShowLog(exceptionMessage).message.isNotEmpty())

            // Verificamos que el resultado en el estado sea null (limpieza)
            assertEquals(null, viewModel.uiState.value.fishResult)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { classifierUseCase(expectedApiString) }
        verify(exactly = 1) { getStringUseCase(R.string.toast_classifier_error) }
    }
}