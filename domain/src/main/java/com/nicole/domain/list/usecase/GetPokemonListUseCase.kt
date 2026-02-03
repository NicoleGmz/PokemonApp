package com.nicole.domain.list.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem

class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(): List<PokemonItem> {
        return repository.getPokemonList()
    }
}