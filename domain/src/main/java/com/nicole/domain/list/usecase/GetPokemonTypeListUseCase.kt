package com.nicole.domain.list.usecase

import com.nicole.domain.PokemonRepository

class GetPokemonTypeListUseCase (
    private val repository: PokemonRepository
){
    suspend operator fun invoke(): List<String> {
        return repository.getPokemonTypeList()
    }
}
