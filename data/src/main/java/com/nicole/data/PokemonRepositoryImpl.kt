package com.nicole.data

import com.nicole.data.mappers.toDomain
import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val api: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonList(): List<PokemonItem>{
        return try {
            println("Getting pokemon list")
            api.getPokemonList().results.map {
                println(it.name)
                println(it.url)
                it.toDomain()
            }
        }catch (e: Exception){
            println(e.message)
            emptyList()
        }
    }

    override fun getPokemonById(id: Int): PokemonItem? {
        return localDataSource.getPokemonById(id)
    }
}