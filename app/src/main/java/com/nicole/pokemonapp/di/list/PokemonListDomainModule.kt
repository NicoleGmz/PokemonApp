package com.nicole.pokemonapp.di.list

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.usecase.GetPokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PokemonListDomainModule {

    @Provides
    fun provideGetPokemonListUseCase(
        repository: PokemonRepository
    ) = GetPokemonListUseCase(repository)

}