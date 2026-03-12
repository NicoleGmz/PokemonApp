package com.nicole.domain.list.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem

class GetPokemonGenerationListUseCase(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.getPokemonGenerationList()
    }
}