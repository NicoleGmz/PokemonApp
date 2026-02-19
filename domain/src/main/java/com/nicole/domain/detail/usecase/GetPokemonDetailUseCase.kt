package com.nicole.domain.detail.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(id: Int): PokemonDetail{
    //    val pokemon = repository.getPokemonById(id)
        return repository.getPokemonById(id)
    }
}