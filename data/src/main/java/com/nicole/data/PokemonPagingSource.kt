package com.nicole.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nicole.data.mappers.toDomain
import com.nicole.data.mappers.toDomainListItem
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PokemonPagingSource(
    private val api: PokemonApi,
    private val searchQuery: String,
    private val typeFilter: String,
    private val generationFilter: String
): PagingSource<Int, PokemonItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonItem> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val offset = page * pageSize

        return try{
            val itemsToFetch = mutableListOf<String>()

            if(searchQuery.isNotBlank()) {

                val queryLowercase = searchQuery.lowercase().trim()
                val isIdQuery = queryLowercase.toIntOrNull() != null

                val pokemonListSummary = api.getPokemonList(limit = 2000, offset = 0)

                val matchedNames = pokemonListSummary.results
                    .filter {
                        val id = it.url.trimEnd('/').substringAfterLast('/')
                        val idInt = id.toIntOrNull() ?: 0
                        if(idInt >= 10000) return@filter false
                        if(isIdQuery){
                            id.startsWith(queryLowercase)
                        }else{
                            it.name.contains(queryLowercase, ignoreCase = true)
                        }
                    }
                    .map { it.name }
                val end = minOf(offset + pageSize, matchedNames.size)

                if (offset < matchedNames.size) {
                    itemsToFetch.addAll(matchedNames.subList(offset, end))
                }
                /*if(page > 0) return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)

                itemsToFetch.add(searchQuery.lowercase().trim())*/
            } else if(typeFilter.isNotBlank()) {

                val typeResponse = api.getPokemonByType(typeFilter.lowercase())
                val pokemonOfThisType = typeResponse.pokemon.map { it.name }
                val end = minOf(offset + pageSize, pokemonOfThisType.size)
                if(offset < pokemonOfThisType.size){
                    itemsToFetch.addAll(pokemonOfThisType.subList(offset, end))
                }

            }
            else{
                val pokemonList = api.getPokemonList(limit = pageSize, offset = offset)
                itemsToFetch.addAll(pokemonList.results.map { it.name })
            }

            val detailedPokemonList = coroutineScope {
                itemsToFetch.map { pokemonName ->
                    async {
                        try {
                            val pokemonDetail = api.getPokemonById(pokemonName)
                            pokemonDetail.toDomainListItem()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }

            val nextKey = if (detailedPokemonList.isEmpty() || searchQuery.isNotBlank()) null else page + 1
            val prevKey = if (page == 0) null else page - 1

            LoadResult.Page(
                data = detailedPokemonList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (e: Exception){
            LoadResult.Error(e )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}