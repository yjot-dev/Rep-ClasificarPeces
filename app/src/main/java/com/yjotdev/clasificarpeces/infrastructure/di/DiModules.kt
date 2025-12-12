package com.yjotdev.clasificarpeces.infrastructure.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.port.ClassifierPort
import com.yjotdev.clasificarpeces.infrastructure.repository.ClassifierRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {
    @Binds
    @Singleton
    abstract fun bindClassifierRepository(
        impl: ClassifierRepository
    ): ClassifierPort
}