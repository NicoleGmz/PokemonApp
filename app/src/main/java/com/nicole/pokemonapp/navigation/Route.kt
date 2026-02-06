package com.nicole.pokemonapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object Welcome: Route

    @Serializable
    data object PokemonList: Route

    @Serializable
    data class PokemonDetail(
        val pokemonId: Int
    ): Route

}