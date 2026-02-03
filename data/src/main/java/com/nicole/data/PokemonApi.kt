package com.nicole.data

import com.nicole.data.model.PokemonListResponse
import retrofit2.http.GET

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(): PokemonListResponse

}