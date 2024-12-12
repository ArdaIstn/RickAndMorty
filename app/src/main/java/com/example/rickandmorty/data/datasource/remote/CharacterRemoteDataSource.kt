package com.example.rickandmorty.data.datasource.remote

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.LocationResult
import com.example.rickandmorty.data.retrofit.CharacterApi
import retrofit2.Response


class CharacterRemoteDataSource(private val characterApi: CharacterApi) {

    suspend fun getLocations(): Response<LocationResult> = characterApi.getLocations()


    suspend fun getMultipleCharactersByIds(ids: String): Response<List<Character>> {
        return characterApi.getMultipleCharactersByIds(ids)
    }

    suspend fun getCharacterById(id: String): Response<Character> {
        return characterApi.getCharacterById(id)

    }
}