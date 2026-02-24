package com.nicole.pokemonapp.ui.pokemonlist.model

import androidx.compose.ui.graphics.Color
import com.nicole.domain.list.model.PokemonItem
import com.nicole.pokemonapp.ui.theme.getPokemonTypeColor

data class PokemonItemUiState(
    val name: String,
    val id: Int,
    val sprite: String,
    val types: List<String>,
    val generation: String,
    val typeColor: List<Color>,
    val gradientColor: List<Color>
)

data class PokemonListUiState(
    val list: List<PokemonItemUiState>
) {
    companion object {
        val DEFAULT = PokemonListUiState(
            list = emptyList()
        )
    }
}

fun List<PokemonItem>.toUiState(): PokemonListUiState {
    println("toUiState: $this")
    return PokemonListUiState(
        list = this.map { it.toUiState() }
    )
}

fun PokemonItem.toUiState(): PokemonItemUiState {
    println("toUiState: $this")
    val typeColor = types.map { getPokemonTypeColor(it) }
    val gradientColor = when{
        typeColor.isEmpty() -> listOf(Color.Transparent, Color.Transparent)
        typeColor.size == 1 -> listOf(typeColor.first(), typeColor.first())
        else -> typeColor
    }

    println(gradientColor)
    return PokemonItemUiState(
        name = name,
        id = id,
        sprite = sprite,
        types = types,
        generation = "",
        typeColor = typeColor,
        gradientColor = gradientColor
    )

}