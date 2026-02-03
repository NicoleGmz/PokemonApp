package com.nicole.domain

import com.nicole.domain.list.model.PokemonItem

interface PokemonRepository {

    suspend fun getPokemonList(): List<PokemonItem>

    fun getPokemonById(id: Int): PokemonItem?
}