package com.example.rickandmorty.data.repository

import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.common.extractId
import com.example.rickandmorty.common.extractIds
import com.example.rickandmorty.data.datasource.CharacterDataSource
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.LocationResult
import com.example.rickandmorty.data.model.Result

class CharacterRepository(private val characterDataSource: CharacterDataSource) {

    suspend fun getResultList(): Resource<List<Result>> {
        return try {
            val response = characterDataSource.getResultList()
            if (response.isSuccessful) {
                val locations = response.body()?.results.orEmpty()
                Resource.Success(locations)
            } else {
                Resource.Fail(response.message())
            }
        } catch (e: Exception) {
            return Resource.Error(e)
        }
    }


    suspend fun getCharactersByLocation(residentList: List<String>): Resource<List<Character>> {
        val ids = residentList.extractIds()

        return try {
            val response = characterDataSource.getCharacterDetailsByIds(ids)
            if (response.isSuccessful) {
                val characters = response.body().orEmpty()
                Resource.Success(characters)
            } else {
                Resource.Fail(response.message())
            }

        } catch (e: Exception) {
            Resource.Error(e) // Hata durumunda istisnayı döndür
        }
    }

    suspend fun getCharacterByLocation(resident: String): Resource<List<Character>> {
        val ids = resident.extractId()
        return try {
            val response = characterDataSource.getCharacterDetailsById(ids)
            if (response.isSuccessful) {
                // API null dönebilir, bu nedenle null kontrolü yapıyoruz
                val character = response.body()
                val characterList = if (character != null) listOf(character) else emptyList()
                Resource.Success(characterList) // Eğer character null ise boş liste dönüyoruz
            } else {
                Resource.Fail(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e) // Hata durumunda istisnayı döndürüyoruz
        }
    }


}
