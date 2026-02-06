package com.nicole.domain.detail.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
){
    companion object{
        val DEFAULT = PokemonDetail(
            id = 0,
            name = "",
            height = 0,
            weight = 0
        )
    }
}
