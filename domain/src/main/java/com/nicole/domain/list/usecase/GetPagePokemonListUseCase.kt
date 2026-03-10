package com.nicole.domain.list.usecase

import androidx.paging.PagingData
import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.flow.Flow

class GetPagePokemonListUseCase(
    private val repository: PokemonRepository
){

    suspend operator fun invoke(
        searchQuery: String,
        typeFilter: String,
        generationFilter: String
    ): Flow<PagingData<PokemonItem>> {
        return repository.getPagedPokemonList(
            searchQuery = searchQuery,
            typeFilter = typeFilter,
            generationFilter = generationFilter
        )
    }
}