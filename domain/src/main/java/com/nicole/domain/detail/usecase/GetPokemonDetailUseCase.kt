package com.nicole.domain.detail.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(id: Int): PokemonDetail{
        return repository.getPokemonById(id)
    }
}