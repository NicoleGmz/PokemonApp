package com.nicole.data.mappers

import com.nicole.data.model.PokemonListItem
import com.nicole.domain.list.model.PokemonItem

fun PokemonListItem.toDomain(): PokemonItem{
    return PokemonItem(
        name = name.replaceFirstChar { it.uppercase() },
        id = getIdFromUrl(url),
        sprite = getSpriteFromId(getIdFromUrl(url))
    )
}

fun getIdFromUrl(url: String): Int{
    return url.split("/").dropLast(1).last().toInt()
}

fun getSpriteFromId(id: Int): String{
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}