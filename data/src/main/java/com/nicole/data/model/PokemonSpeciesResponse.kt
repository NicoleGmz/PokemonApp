package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("generation") val generation: Generation
)

data class Generation(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)