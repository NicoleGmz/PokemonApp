package com.nicole.domain.detail.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) {

    operator fun invoke(): PokemonDetail{
        return PokemonDetail.DEFAULT
        TODO("Not yet implemented")
        //return repository.getPokemonById(id) ?: PokemonDetail.DEFAULT
    }
}