package com.nicole.data.mappers

import com.nicole.data.model.PokemonListItem
import com.nicole.domain.list.model.PokemonItem

fun PokemonListItem.toDomain(): PokemonItem{
    return PokemonItem(
        name = name.replaceFirstChar { it.uppercase() },
        id = getIdFromUrl(url),
        sprite = getPokemonSprite(getIdFromUrl(url)),
        image = getPokemonImage(getIdFromUrl(url))
    )
}

fun getIdFromUrl(url: String): Int{
    return url.split("/").dropLast(1).last().toInt()
}

fun getPokemonSprite(id: Int): String{
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

fun getPokemonImage(id: Int): String{
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}