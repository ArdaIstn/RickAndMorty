package com.example.rickandmorty.data.datasource.remote

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.LocationResult
import com.example.rickandmorty.data.retrofit.CharacterApi
import retrofit2.Response


class CharacterRemoteDataSource(private val characterApi: CharacterApi) {

    suspend fun getResultList(): Response<LocationResult> = characterApi.getLocations()

    // Karakterlerin detaylarını çekmek için
    suspend fun getCharacterDetailsByIds(ids: String): Response<List<Character>> {
        return characterApi.getMultipleCharacters(ids)
    }

    suspend fun getCharacterDetailsById(id: String): Response<Character> {
        return characterApi.getCharacterDetailsById(id)

    }
}