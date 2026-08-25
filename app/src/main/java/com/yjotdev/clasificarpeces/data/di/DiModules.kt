package com.yjotdev.clasificarpeces.data.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Provides
import retrofit2.Retrofit
import com.yjotdev.clasificarpeces.data.repository.SpeciesRepositoryImpl
import com.yjotdev.clasificarpeces.data.repository.StringRepositoryImpl
import com.yjotdev.clasificarpeces.data.remote.network.RetrofitBuilder
import com.yjotdev.clasificarpeces.data.remote.api.SpeciesApi
import com.yjotdev.clasificarpeces.domain.repository.SpeciesRepository
import com.yjotdev.clasificarpeces.domain.repository.StringRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindClassifierRepository(
        impl: SpeciesRepositoryImpl
    ): SpeciesRepository

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: StringRepositoryImpl
    ): StringRepository

    // --- PROVIDERS (Instancias externas) ---
    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit {
            return retrofitBuilder.getRetrofitInstance()
        }

        @Provides
        @Singleton
        fun provideClassifierApi(retrofit: Retrofit): SpeciesApi {
            return retrofit.create(SpeciesApi::class.java)
        }
    }
}