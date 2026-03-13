package com.nicole.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nicole.data.mappers.toDomainListItem
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class PokemonPagingSource(
    private val api: PokemonApi,
    private val searchQuery: String,
    private val typeFilter: Set<String>,
    private val generationFilter: Set<String>
) : PagingSource<Int, PokemonItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonItem> {
        val pageSize = params.loadSize
        val offset = params.key ?: 0

        if (typeFilter.size > 2) {
            Timber.d("Type filter size is greater than 2")
            return LoadResult.Page(
                emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return try {
            val itemsToFetch = mutableSetOf<String>()
            var validIds: Set<Int>? = null

            var endOfPaginationReached = false

            if (typeFilter.isNotEmpty()) {
                var combinedTypeIds: Set<Int>? = null

                for (type in typeFilter) {
                    val typeResponse = api.getPokemonByType(type.lowercase())
                    val pokemonOfThisType = typeResponse.pokemon
                        .mapNotNull {
                            val id =
                                it.pokemon.url.trimEnd('/').substringAfterLast('/').toIntOrNull()
                                    ?: 0
                            if (id in 1..9999) id else null
                        }.toSet()
                    //.map { it.pokemon.name }
                    combinedTypeIds = combinedTypeIds?.intersect(pokemonOfThisType)
                        ?: pokemonOfThisType
                }
                validIds = combinedTypeIds
            }

            if (generationFilter.isNotEmpty()) {
                val combinedGenerationIds = mutableSetOf<Int>()

                for (generation in generationFilter) {
                    val genFormatted = generation.lowercase().replace(" ", "-")
                    val generationResponse = api.getGeneration(genFormatted)

                    val pokemonOfThisGeneration = generationResponse.pokemonSpecies.mapNotNull {
                        val id = it.url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
                        if (id in 1..9999) id else null
                    }.toSet()

                    combinedGenerationIds.addAll(pokemonOfThisGeneration)
                }

                validIds = validIds?.intersect(combinedGenerationIds)
                    ?: combinedGenerationIds
            }

            if (searchQuery.isNotBlank()) {

                val queryLowercase = searchQuery.lowercase().trim()
                val isIdQuery = queryLowercase.toIntOrNull() != null

                val pokemonListSummary = api.getPokemonList(limit = 2000, offset = 0)

                val searchIds: Set<Int> = pokemonListSummary.results
                    .mapNotNull {
                        val id = it.url.trimEnd('/').substringAfterLast('/')
                        val idInt = id.toIntOrNull() ?: 0
                        if (idInt >= 10000) {
                            null
                        } else {
                            val matches = if (isIdQuery) {
                                id.startsWith(queryLowercase)
                            } else {
                                it.name.contains(queryLowercase, ignoreCase = true)
                            }
                            if (matches) idInt else null
                        }
                    }.toSet()

                validIds = validIds?.intersect(searchIds) ?: searchIds
            }

            if (validIds != null) {
                val sortedIds = validIds.sorted()
                val end = minOf(offset + pageSize, sortedIds.size)
                if (offset < sortedIds.size) {
                    itemsToFetch.addAll(sortedIds.subList(offset, end).map { it.toString() })
                }
                endOfPaginationReached = (offset + pageSize) >= sortedIds.size
            } else {
                val pokemonList = api.getPokemonList(limit = pageSize, offset = offset)
                itemsToFetch.addAll(
                    pokemonList.results
                        .mapNotNull {
                            val id = it.url.extractIdFromUrl()
                            if (id in 1..9999) id.toString() else null
                        }
                )

                endOfPaginationReached = pokemonList.results.isEmpty() || pokemonList.next == null
            }

            val detailedPokemonList = coroutineScope {
                itemsToFetch.map { pokemonName ->
                    async {
                        try {
                            val pokemonDetail = api.getPokemon(pokemonName)
                            pokemonDetail.toDomainListItem()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }

            val nextKey = if (endOfPaginationReached) null else offset + pageSize
            val prevKey = if (offset == 0) null else maxOf(0, offset - pageSize)

            LoadResult.Page(
                data = detailedPokemonList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Timber.e("Error getting pokemon list: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun String.extractIdFromUrl(): Int =
        this.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0

}