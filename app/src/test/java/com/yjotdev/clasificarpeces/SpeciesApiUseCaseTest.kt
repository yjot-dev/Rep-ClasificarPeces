package com.yjotdev.clasificarpeces

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.usecase.SpeciesApiUseCase
import com.yjotdev.clasificarpeces.domain.core.Result
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

/**
 * Pruebas unitarias para [SpeciesApiUseCase].
 * Sigue el perfil de Arquitectura Hexagonal (A.1.3) y las reglas de estilo (A.3).
 */
class SpeciesApiUseCaseTest {

    private lateinit var speciesApiRepository: SpeciesApiRepository
    private lateinit var speciesApiUseCase: SpeciesApiUseCase

    @Before
    fun setup() {
        speciesApiRepository = mockk()
        speciesApiUseCase = SpeciesApiUseCase(speciesApiRepository)
    }

    /**
     * Verifica que cuando el puerto funciona exitosamente, el caso de uso retorna una lista de entidades.
     */
    @Test
    fun whenSpeciesApiUseCaseIsInvokedSuccessfullyThenItReturnsAListOfEntities() = runTest {
        // GIVEN
        val fakeSpeciesList = listOf(
            SpeciesModel(commonName = "Pez Dorado", scientificName = "Carassius auratus"),
            SpeciesModel(commonName = "Tiburón", scientificName = "Carcharodon carcharias")
        )
        // Entrenamos al puerto para devolver un Result.Success
        coEvery { speciesApiRepository.seleccionarEspecies(any()) } returns Result.Success(fakeSpeciesList)

        // WHEN
        val result = speciesApiUseCase("es")

        // THEN
        assertTrue(result is Result.Success)
        assertEquals(fakeSpeciesList, (result as Result.Success).data)

        // Verificación de interacción con la capa de infraestructura/puerto
        coVerify(exactly = 1) { speciesApiRepository.seleccionarEspecies(any()) }
    }

    /**
     * Verifica que cuando el puerto falla, el caso de uso propaga el error correctamente.
     */
    @Test
    fun whenSpeciesApiUseCaseFailsThenItReturnsAnError() = runTest {
        // GIVEN
        val exception = Exception("Error al procesar el modelo TFLite")
        // Entrenamos al puerto para devolver un Result.Error
        coEvery { speciesApiRepository.seleccionarEspecies(any()) } returns Result.Error(exception)

        // WHEN
        val result = speciesApiUseCase("es")

        // THEN
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)

        // Verificación de interacción
        coVerify(exactly = 1) { speciesApiRepository.seleccionarEspecies(any()) }
    }
}