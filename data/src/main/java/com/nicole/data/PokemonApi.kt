package com.nicole.data

import com.nicole.data.model.PokemonDetailResponse
import com.nicole.data.model.PokemonListResponse
import com.nicole.data.model.PokemonSpeciesResponse
import com.nicole.data.model.PokemonTypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonById(
        @Path("id") id: Int
    ): PokemonDetailResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonById(
        @Path("name") name: String
    ): PokemonDetailResponse


    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpeciesById(
        @Path("id") id: Int
    ): PokemonSpeciesResponse

    @GET("generation/{id}")
    suspend fun getPokemonSpeciesById(
        @Path("id") id: String
    ): PokemonSpeciesResponse

    @GET("type/{type}")
    suspend fun getPokemonByType(
        @Path("type") type: String
    ): PokemonTypeResponse

}