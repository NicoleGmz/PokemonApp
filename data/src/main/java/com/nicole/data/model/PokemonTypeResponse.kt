package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class PokemonTypeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("pokemon") val pokemon: List<PokemonTypeItem>
)

data class PokemonTypeItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
