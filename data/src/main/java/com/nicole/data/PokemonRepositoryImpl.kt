package com.nicole.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nicole.data.mappers.toDomain
import com.nicole.data.mappers.toDomainListItem
import com.nicole.data.mappers.toPokemonListItemDomain
import com.nicole.data.model.PokemonDetailResponse
import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonTypeList(): List<String> {
        val typeList = api.getTypesList().results
        return typeList.map { type -> type.name.replaceFirstChar { it.uppercase() } }
    }

    override suspend fun getPokemonGenerationList(): List<String> {
        val generationList = api.getGenerationList().results
        return generationList.map {
            val generation = formatGenerationName(it.name)
            formatGenerationName(generation)
        }
    }

    override suspend fun getPokemonList(): List<PokemonItem> = try {
            println("Getting pokemon list")
            val pokemonList = api.getPokemonList(limit = 151, offset = 0)
            coroutineScope {
                val resultList = pokemonList.results.map{
                    async {
                        val pokemonListItem = it.toPokemonListItemDomain()
                        try {
                            val pokemonDetail = getPokemonById(pokemonListItem.id)
                            pokemonListItem.copy(
                                types = pokemonDetail.types,
                                generation = pokemonDetail.generation
                            )
                        } catch (e: Exception) {
                            println("Failed to fetch types for ${pokemonListItem.name}: ${e.message}")
                            pokemonListItem
                        }
                    }
                }
                resultList.awaitAll()
            }
        } catch (e: Exception) {
            println("Error getting the list:${e.message}")
            emptyList()
        }

    override suspend fun getPagedPokemonList(
        searchQuery: String,
        typeFilter: Set<String>,
        generationFilter: Set<String>
    ): Flow<PagingData<PokemonItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PokemonPagingSource(
                    api = api,
                    searchQuery = searchQuery,
                    typeFilter = typeFilter,
                    generationFilter = generationFilter
                )
            }
        ).flow
    }

    override suspend fun getPokemonById(id: Int): PokemonDetail = try {
            println("Getting pokemon $id")
            val pokemon = api.getPokemon(id.toString()).toDomain()
            val generation = api.getPokemonSpecies(id).generation.name
            println(generation)
            pokemon.copy(generation = formatGenerationName(generation))
        }catch (e: Exception){
            println("Error getting pokemon $id: ${e.message}")
            PokemonDetail.DEFAULT
        }

    private fun getGenerationFromUrl(url: String): String = url.split("/").dropLast(1).last()

    private fun formatGenerationName(generation: String): String = generation
        .split("-")
        .mapIndexed { index, word ->
            if(index == 0){
                word.replaceFirstChar { it.uppercase() }
            }else {
                word.uppercase()
            }
        }
        .joinToString(" ")

}