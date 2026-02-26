package com.yjotdev.clasificarpeces

import android.graphics.Bitmap
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.entity.ClassifierEntity
import com.yjotdev.clasificarpeces.domain.usecase.ClassifierUseCase
import com.yjotdev.clasificarpeces.domain.port.ClassifierPort

/**
 * Pruebas unitarias para el caso de uso de clasificación de imágenes.
 */
class ClassifierUseCaseTest {

    private lateinit var classifierPort: ClassifierPort
    private lateinit var classifierUseCase: ClassifierUseCase
    private val mockBitmap: Bitmap = mockk()

    @Before
    fun setUp() {
        classifierPort = mockk()
        classifierUseCase = ClassifierUseCase(classifierPort)
    }

    @Test
    fun whenClassifierUseCaseIsInvokedSuccessfullyThenItReturnsAListOfClassifications() = runTest {
        // Given
        val fakeClassifications = listOf(
            ClassifierEntity(label = "Betta", score = 0.9f),
            ClassifierEntity(label = "Guppy", score = 0.05f)
        )
        coEvery { classifierPort.classify(mockBitmap) } returns Result.Success(fakeClassifications)

        // When
        val result = classifierUseCase(mockBitmap)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeClassifications, (result as Result.Success).data)
        coVerify(exactly = 1) { classifierPort.classify(mockBitmap) }
    }

    @Test
    fun whenClassifierUseCaseFailsThenItReturnsAnError() = runTest {
        // Given
        val errorMessage = Exception("Model failed to classify")
        coEvery { classifierPort.classify(mockBitmap) } returns Result.Error(errorMessage)

        // When
        val result = classifierUseCase(mockBitmap)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).exception)
        coVerify(exactly = 1) { classifierPort.classify(mockBitmap) }
    }
}
