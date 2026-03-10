package com.nicole.domain

import androidx.paging.PagingData
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(): List<PokemonItem>

    suspend fun getPagedPokemonList(
        searchQuery: String,
        typeFilter: String,
        generationFilter: String
    ): Flow<PagingData<PokemonItem>>

    suspend fun getPokemonById(id: Int): PokemonDetail
}