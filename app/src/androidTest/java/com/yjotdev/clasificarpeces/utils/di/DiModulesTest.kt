package com.yjotdev.clasificarpeces.utils.di

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.Binds
import dagger.Provides
import javax.inject.Singleton
import dagger.Module
import com.yjotdev.clasificarpeces.data.di.DiModules
import com.yjotdev.clasificarpeces.data.local.dao.SpeciesDao
import com.yjotdev.clasificarpeces.data.local.database.SpeciesDatabase
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository
import com.yjotdev.clasificarpeces.domain.repository.StringRepository
import com.yjotdev.clasificarpeces.utils.repository.FakeSpeciesApiRepositoryImpl
import com.yjotdev.clasificarpeces.utils.repository.FakeSpeciesDaoRepositoryImpl
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
    abstract fun bindSpeciesApiRepository(
        impl: FakeSpeciesApiRepositoryImpl
    ): SpeciesApiRepository

    @Binds
    @Singleton
    abstract fun bindSpeciesDaoRepository(
        impl: FakeSpeciesDaoRepositoryImpl
    ): SpeciesDaoRepository

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: FakeStringRepositoryImpl
    ): StringRepository

    // --- PROVIDERS (Instancias externas) ---
    companion object {
        @Provides
        @Singleton
        fun provideFakeDatabase(@ApplicationContext context: Context): SpeciesDatabase =
            Room.inMemoryDatabaseBuilder(
                context,
                SpeciesDatabase::class.java,
            ).build()

        @Provides
        @Singleton
        fun provideFakeSpeciesDao(database: SpeciesDatabase): SpeciesDao =
            database.speciesDao()
    }
}