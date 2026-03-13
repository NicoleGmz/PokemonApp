package com.nicole.data

import com.nicole.data.model.CommonListResponse
import com.nicole.data.model.GenerationResponse
import com.nicole.data.model.PokemonDetailResponse
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
    ): CommonListResponse

    @GET("generation")
    suspend fun getGenerationList(): CommonListResponse

    @GET("type")
    suspend fun getTypesList(): CommonListResponse

    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(
        @Path("idOrName") idOrName: String
    ): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesResponse

    @GET("generation/{idOrName}")
    suspend fun getGeneration(
        @Path("idOrName") idOrName: String
    ): GenerationResponse

    @GET("type/{type}")
    suspend fun getPokemonByType(
        @Path("type") type: String
    ): PokemonTypeResponse
}
