package com.nicole.domain.list.model

data class PokemonItem(
    val name: String,
    val id: Int,
    val sprite: String
){
    companion object{
        val DEFAULT = PokemonItem(
            name = "",
            id = 0,
            sprite = ""
        )
    }
}
