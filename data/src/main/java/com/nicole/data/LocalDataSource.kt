package com.nicole.data

import com.nicole.domain.list.model.PokemonItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor() {
    private val pokemonList = listOf(
        PokemonItem(
            name = "Bulbasaur",
            id = 1
        ),
        PokemonItem(
            name = "Ivysaur",
            id = 2
        ),
        PokemonItem(
            name = "Venusaur",
            id = 3
        ),
        PokemonItem(
            name = "Charmander",
            id = 4
        ),
        PokemonItem(
            name = "Charmeleon",
            id = 5
        ),
        PokemonItem(
            name = "Charizard",
            id = 6
        ),
        PokemonItem(
            name = "Squirtle",
            id = 7
        ),
        PokemonItem(
            name = "Wartortle",
            id = 8
        ),
        PokemonItem(
            name = "Blastoise",
            id = 9
        ),
        PokemonItem(
            name = "Caterpie",
            id = 10
        ),
        PokemonItem(
            name = "Metapod",
            id = 11
        ),
        PokemonItem(
            name = "Butterfree",
            id = 12
        ),
        PokemonItem(
            name = "Weedle",
            id = 13
        ),
        PokemonItem(
            name = "Kakuna",
            id = 14
        ),
        PokemonItem(
            name = "Beedrill",
            id = 15
        ),
        PokemonItem(
            name = "Pidgey",
            id = 16
        ),
    )

    fun getPokemonList(): List<PokemonItem> {
        return pokemonList
    }

    fun getPokemonById(id: Int): PokemonItem? {
        if(id > pokemonList.size) throw Exception("Pokemon not found")
        return pokemonList.find { it.id == id }
    }
}