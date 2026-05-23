package com.yjotdev.clasificarpeces.utils.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.Binds
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.data.di.DiModules
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.utils.repository.FakeClassifierRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModules::class] // Nombre del módulo real
)
@Suppress("unused")
abstract class DiModulesTest {
    @Binds
    @Singleton
    abstract fun bindClassifierRepository(
        impl: FakeClassifierRepository
    ): ClassifierRepository
}