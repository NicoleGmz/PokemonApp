package com.nicole.pokemonapp.di.detail

import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.usecase.GetPokemonDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PokemonDetailDomainModule {

    @Provides
    fun provideGetPokemonDetailUseCase(
        repository: PokemonRepository
    ) = GetPokemonDetailUseCase(repository)

}