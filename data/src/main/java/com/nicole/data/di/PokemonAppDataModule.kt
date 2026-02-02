package com.nicole.data.di

import com.nicole.data.PokemonRepositoryImpl
import com.nicole.domain.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class PokemonAppDataModule {

    @Singleton
    @Binds
    abstract fun bindPokemonRepository(
        repository: PokemonRepositoryImpl
    ): PokemonRepository

}