package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class PokemonTypeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("pokemon") val pokemon: List<TypePokemonSlotDto>
)

data class TypePokemonSlotDto(
    @SerializedName("pokemon") val pokemon: CommonItemApiResource
)
