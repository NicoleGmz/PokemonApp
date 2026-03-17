package com.nicole.domain.list.usecase

import androidx.paging.PagingData
import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
){

    operator fun invoke(
        searchQuery: String,
        typeFilter: Set<String>,
        generationFilter: Set<String>
    ): Flow<PagingData<PokemonItem>> {
        return repository.getPagedPokemonList(
            searchQuery = searchQuery,
            typeFilter = typeFilter,
            generationFilter = generationFilter
        )
    }
}