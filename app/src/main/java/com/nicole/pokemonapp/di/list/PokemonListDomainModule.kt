package com.nicole.pokemonapp.di.list

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.usecase.GetPagePokemonListUseCase
import com.nicole.domain.list.usecase.GetPokemonGenerationListUseCase
import com.nicole.domain.list.usecase.GetPokemonTypeListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PokemonListDomainModule {

    @Provides
    fun provideGetPokemonTypeListUseCase(
        repository: PokemonRepository
    ) = GetPokemonTypeListUseCase(repository)

    @Provides
    fun provideGetPokemonGenerationListUseCase(
        repository: PokemonRepository
    ) = GetPokemonGenerationListUseCase(repository)

    @Provides
    fun provideGetPagePokemonListUseCase(
        repository: PokemonRepository
    ) = GetPagePokemonListUseCase(repository)

}