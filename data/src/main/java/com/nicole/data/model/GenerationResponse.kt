package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class GenerationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("pokemon_species") val pokemonSpecies: List<GenerationItem>
)

data class GenerationItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)