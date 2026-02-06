package com.nicole.pokemonapp.ui.pokemondetail.model

data class PokemonDetailUiState(
    val name: String
) {
    companion object {
        val DEFAULT = PokemonDetailUiState(
            name = ""
        )
    }
}
    