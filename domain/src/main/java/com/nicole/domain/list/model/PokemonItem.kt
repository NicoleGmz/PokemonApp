package com.nicole.domain.list.model

data class PokemonItem(
    val name: String,
    val id: Int
){
    companion object{
        val DEFAULT = PokemonItem(
            name = "",
            id = 0
        )
    }
}
