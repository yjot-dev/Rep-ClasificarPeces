package com.yjotdev.clasificarpeces

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.yjotdev.clasificarpeces.domain.repository.SpeciesRepository
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesUseCase
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

/**
 * Pruebas unitarias para el caso de uso de clasificación de imágenes.
 */
class SpeciesUseCaseTest {

    private lateinit var speciesRepository: SpeciesRepository
    private lateinit var speciesUseCase: SpeciesUseCase

    @Before
    fun setup() {
        // Inicializamos el repositorio simulado
        speciesRepository = mockk()
        // Pasamos el mock al caso de uso
        speciesUseCase = SpeciesUseCase(speciesRepository)
    }

    @Test
    fun whenSpeciesUseCaseIsInvokedSuccessfullyThenItReturnListOfSpecies() = runBlocking {
        // Given
        val fakeSpeciesList = listOf(
            SpeciesModel(commonName = "Pez Dorado", scientificName = "Carassius auratus"),
            SpeciesModel(commonName = "Tiburón", scientificName = "Carcharodon carcharias")
        )
        coEvery { speciesRepository.seleccionarEspecies(any(), any()) } returns Result.Success(fakeSpeciesList)

        // When
        val result = speciesUseCase("pez", "es")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeSpeciesList, (result as Result.Success).data)
        coVerify(exactly = 1) { speciesRepository.seleccionarEspecies(any(), any()) }
    }

    @Test
    fun whenSpeciesUseCaseIsInvokedWithErrorThenItReturnResultError() = runBlocking {
        // Given
        val exception = Exception("Error al consultar especies")
        coEvery { speciesRepository.seleccionarEspecies(any(), any()) } returns Result.Error(exception)

        // When
        val result = speciesUseCase("pez", "es")

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { speciesRepository.seleccionarEspecies(any(), any()) }
    }

    @Test
    fun whenSpeciesUseCaseIsInvokedWithNullSearchTextThenItReturnAllSpecies() = runBlocking {
        // Given
        val fakeSpeciesList = listOf(
            SpeciesModel(commonName = "Pez Dorado", scientificName = "Carassius auratus"),
            SpeciesModel(commonName = "Tiburón", scientificName = "Carcharodon carcharias")
        )
        coEvery { speciesRepository.seleccionarEspecies(null, any()) } returns Result.Success(fakeSpeciesList)

        // When
        val result = speciesUseCase(null, "es")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeSpeciesList, (result as Result.Success).data)
        coVerify(exactly = 1) { speciesRepository.seleccionarEspecies(null, any()) }
    }
}