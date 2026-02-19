package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonListItem>
)

data class PokemonListItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    val id: Int,
    val sprite: String
)
