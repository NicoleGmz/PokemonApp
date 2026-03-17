package com.nicole.domain.list.usecase

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem
import javax.inject.Inject

class GetPokemonGenerationListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.getPokemonGenerationList()
    }
}