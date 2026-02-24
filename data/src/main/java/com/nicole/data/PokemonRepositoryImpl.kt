package com.nicole.data

import com.nicole.data.mappers.toDomain
import com.nicole.data.model.PokemonDetailResponse
import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonList(): List<PokemonItem> = try {
            println("Getting pokemon list")
            val pokemonList = api.getPokemonList(limit = 151, offset = 0)
            coroutineScope {
                val resultList = pokemonList.results.map { pokemonListItem ->
                    async {
                        val pokemon = pokemonListItem.toDomain()
                        try {
                            println("Getting pokemon ${pokemonListItem.name}")
                            println(pokemon)
                            val pokemonDetail = getPokemonById(pokemon.id)
                            pokemon.copy(types = pokemonDetail.types)
                        } catch (e: Exception) {
                            println("Failed to fetch types for ${pokemonListItem.name}: ${e.message}")
                            pokemon
                        }
                    }
                }
                resultList.awaitAll()
            }
        } catch (e: Exception) {
            println("Error getting the list:${e.message}")
            emptyList()
        }

    override suspend fun getPokemonById(id: Int): PokemonDetail = try {
            println("Getting pokemon $id")
            val pokemon = api.getPokemonById(id).toDomain()
            val generation = api.getPokemonSpeciesById(id).generation.url
            pokemon.copy(generation = getGenerationFromUrl(generation))
        }catch (e: Exception){
            PokemonDetail.DEFAULT
        }

    private fun getGenerationFromUrl(url: String): String = url.split("/").dropLast(1).last()
    
}