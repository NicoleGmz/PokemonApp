package com.nicole.pokemonapp.ui.pokemonlist.model

import com.nicole.domain.list.model.PokemonItem


data class PokemonListUiState(
    val list: List<PokemonItem>
) {
    companion object {
        val DEFAULT = PokemonListUiState(
            list = emptyList()
        )
    }
}

fun List<PokemonItem>.toUiState(): PokemonListUiState {
    return PokemonListUiState(
        list = this)
}