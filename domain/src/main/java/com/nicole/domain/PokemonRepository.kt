package com.nicole.domain

import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem

interface PokemonRepository {

    suspend fun getPokemonList(): List<PokemonItem>

    suspend fun getPokemonById(id: Int): PokemonDetail
}