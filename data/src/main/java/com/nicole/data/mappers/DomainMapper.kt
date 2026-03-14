package com.nicole.data.mappers

import com.nicole.data.model.PokemonDetailResponse
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem

fun PokemonDetailResponse.toDomain(): PokemonDetail{
    return PokemonDetail(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        height = height,
        weight = weight,
        sprite = sprites.other.officialArtwork.frontDefault,
        types = types.map {
            it.type.name.replaceFirstChar { name ->
                name.uppercase() }
        },
        generation = "",
        stats = HashMap(
            stats.associate {
                it.stat.name to it.baseStat
            }
        )
    )
}

fun PokemonDetailResponse.toDomainListItem(): PokemonItem{
    return PokemonItem(
        name = name.replaceFirstChar { it.uppercase() },
        id = id,
        sprite = getSpriteFromId(id),
        types = types.map { it.type.name },
        generation = ""
    )
}

fun getSpriteFromId(id: Int): String{
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}
