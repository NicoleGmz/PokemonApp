package com.nicole.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nicole.data.mappers.toDomain
import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonTypeList(): List<String> {
        Timber.d("Getting pokemon type list")
        val typeList = api.getTypesList().results
        return typeList.map { type -> type.name.replaceFirstChar { it.uppercase() } }
    }

    override suspend fun getPokemonGenerationList(): List<String> {
        Timber.d("Getting pokemon generation list")
        val generationList = api.getGenerationList().results
        return generationList.map {
            formatGenerationName(it.name)
        }
    }

    override suspend fun getPagedPokemonList(
        searchQuery: String,
        typeFilter: Set<String>,
        generationFilter: Set<String>
    ): Flow<PagingData<PokemonItem>> {
        Timber.d("Getting paged pokemon list")
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
            Timber.d("Getting pokemon detail for $id")
            val pokemon = api.getPokemon(id.toString()).toDomain()
            val generation = api.getPokemonSpecies(id).generation.name
            pokemon.copy(generation = formatGenerationName(generation))
        }catch (e: Exception){
            Timber.e("Error getting pokemon detail: ${e.message}")
            PokemonDetail.DEFAULT
        }

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