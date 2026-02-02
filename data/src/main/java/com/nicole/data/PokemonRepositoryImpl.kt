package com.nicole.data

import com.nicole.domain.PokemonRepository
import com.nicole.domain.list.model.PokemonItem
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : PokemonRepository {
    override fun getPokemonList(): List<PokemonItem>{
        return localDataSource.getPokemonList()
    }

    override fun getPokemonById(id: Int): PokemonItem? {
        return localDataSource.getPokemonById(id)
    }
}