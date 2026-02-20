package com.nicole.pokemonapp.ui.pokemondetail.model

import com.nicole.domain.detail.model.PokemonDetail

data class PokemonDetailUiState(
    //val pokemonDetail: PokemonDetail
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprite: String?,
    val types: List<String>,
    val generation: String,
    val stats: List<List<String>>,
) {
    companion object {
        val DEFAULT = PokemonDetailUiState(
            id = 0,
            name = "",
            height = 0,
            weight = 0,
            sprite = "",
            types = emptyList(),
            generation = "",
            stats = emptyList()
        )
    }
}

fun PokemonDetail.toUiState(): PokemonDetailUiState {
    println("pokemon: $this")
    return PokemonDetailUiState(
        id = id,
        name = name,
        height = height,
        weight = weight,
        sprite = sprite,
        types = types,
        generation = generation,
        stats = listStats(stats)
    )
}

fun formatStatPercentage(statValue: Int, maxValue: Int = 255): Float{
    return (statValue.toFloat() / maxValue.toFloat())
}

fun formatStatName(statName: String): String{
    return when(statName){
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> statName.replaceFirstChar { it.uppercase() }
    }
}

fun listStats(stats: Map<String, Int>): List<List<String>>{
    val result = mutableListOf<List<String>>()
    for (stat in stats){
        result.add(listOf(formatStatName(stat.key), stat.value.toString(), formatStatPercentage(stat.value).toString()))
    }
    return result
}


    