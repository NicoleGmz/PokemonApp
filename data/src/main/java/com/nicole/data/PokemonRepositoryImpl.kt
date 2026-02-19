package com.nicole.data

import com.nicole.data.mappers.toDomain
import com.nicole.data.model.PokemonDetailResponse
import com.nicole.domain.PokemonRepository
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.domain.list.model.PokemonItem
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonList(): List<PokemonItem>{
        return try {
            println("Getting pokemon list")
            api.getPokemonList(
                limit = 151,
                offset = 0
            ).results.map {
                it.toDomain()
            }
        }catch (e: Exception){
            println(e.message)
            emptyList()
        }
    }

    override suspend fun getPokemonById(id: Int): PokemonDetail {
        return try {
            api.getPokemonById(id).toDomain()
        }catch (e: Exception){
            PokemonDetail.DEFAULT
        }
    }
}