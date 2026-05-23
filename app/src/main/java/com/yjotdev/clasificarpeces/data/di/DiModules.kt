package com.yjotdev.clasificarpeces.data.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.ClassifierRepository
import com.yjotdev.clasificarpeces.data.repository.ClassifierRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {
    @Binds
    @Singleton
    abstract fun bindClassifierRepository(
        impl: ClassifierRepositoryImpl
    ): ClassifierRepository
}