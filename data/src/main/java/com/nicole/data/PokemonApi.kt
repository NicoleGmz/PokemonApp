package com.nicole.data

import com.nicole.data.model.PokemonListResponse
import com.nicole.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonById(
        @Path("id") id: Int
    ): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(name: String): PokemonResponse

}