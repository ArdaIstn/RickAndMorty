package com.example.rickandmorty.data.retrofit

import com.example.rickandmorty.common.Constants.CHARACTERS_ENDPOINT
import com.example.rickandmorty.common.Constants.CHARACTER_ENDPOINT
import com.example.rickandmorty.common.Constants.LOCATIONS_ENDPOINT
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.LocationResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {
    @GET(LOCATIONS_ENDPOINT)
    suspend fun getLocations(): Response<LocationResult>

    @GET(CHARACTERS_ENDPOINT)
    suspend fun getMultipleCharacters(@Path("ids") ids: String): Response<List<Character>>

    @GET(CHARACTER_ENDPOINT)
    suspend fun getCharacterDetailsById(
        @Path("id") id: String
    ): Response<Character>


}