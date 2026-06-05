package com.yjotdev.clasificarpeces.utils.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.Binds
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.data.di.DiModules
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.domain.repository.StringRepository
import com.yjotdev.clasificarpeces.utils.repository.FakeClassifierRepositoryImpl
import com.yjotdev.clasificarpeces.utils.repository.FakeStringRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModules::class] // Nombre del módulo real
)
@Suppress("unused")
abstract class DiModulesTest {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindClassifierRepository(
        impl: FakeClassifierRepositoryImpl
    ): ClassifierRepository

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: FakeStringRepositoryImpl
    ): StringRepository

    // --- PROVIDERS (Instancias externas) ---
    // No son necesarios aqui
}