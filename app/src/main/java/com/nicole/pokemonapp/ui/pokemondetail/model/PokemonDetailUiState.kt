package com.nicole.pokemonapp.ui.pokemondetail.model

import com.nicole.domain.detail.model.PokemonDetail

data class PokemonDetailUiState(
    val pokemonDetail: PokemonDetail
) {
    companion object {
        val DEFAULT = PokemonDetailUiState(
            pokemonDetail = PokemonDetail.DEFAULT
        )
    }
}

fun PokemonDetail.toUiState(): PokemonDetailUiState {
    println("pokemon: $this")
    return PokemonDetailUiState(
        pokemonDetail = this
    )
}


    