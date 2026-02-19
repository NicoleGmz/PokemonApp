package com.nicole.data.mappers

import com.nicole.data.model.PokemonDetailResponse
import com.nicole.data.model.PokemonListItem
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem

fun PokemonListItem.toDomain(): PokemonItem{
    return PokemonItem(
        name = name.replaceFirstChar { it.uppercase() },
        id = getIdFromUrl(url),
        sprite = getSpriteFromId(getIdFromUrl(url))
    )
}

fun PokemonDetailResponse.toDomain(): PokemonDetail{
    return PokemonDetail(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        height = height,
        weight = weight,
        sprite = sprites.frontDefault ?: "",
        types = types.map {
            it.type.name.replaceFirstChar { name ->
                name.uppercase() }
        }
    )
}

fun getIdFromUrl(url: String): Int{
    return url.split("/").dropLast(1).last().toInt()
}

fun getSpriteFromId(id: Int): String{
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}