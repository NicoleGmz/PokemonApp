package com.nicole.domain.list.usecase

import com.nicole.domain.PokemonRepository
import javax.inject.Inject

class GetPokemonTypeListUseCase @Inject constructor (
    private val repository: PokemonRepository
){
    suspend operator fun invoke(): List<String> {
        return repository.getPokemonTypeList()
    }
}
