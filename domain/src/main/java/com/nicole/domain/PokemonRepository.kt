package com.nicole.domain

import androidx.paging.PagingData
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonTypeList(): List<String>

    suspend fun getPokemonGenerationList(): List<String>

    fun getPagedPokemonList(
        searchQuery: String,
        typeFilter: Set<String>,
        generationFilter: Set<String>
    ): Flow<PagingData<PokemonItem>>

    suspend fun getPokemonById(id: Int): PokemonDetail
}