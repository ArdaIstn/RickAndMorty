package com.example.rickandmorty.data.repository

import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.common.extractId
import com.example.rickandmorty.common.extractIds
import com.example.rickandmorty.data.datasource.local.CharacterLocalDataSource
import com.example.rickandmorty.data.datasource.remote.CharacterRemoteDataSource
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.CharacterFav
import com.example.rickandmorty.data.model.Result
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterLocalDataSource: CharacterLocalDataSource
) {

    // Api
    suspend fun getLocations(): Resource<List<Result>> {
        return try {
            val response = characterRemoteDataSource.getLocations()
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


    suspend fun getMultipleCharactersByIds(residentList: List<String>): Resource<List<Character>> {
        val ids = residentList.extractIds()

        return try {
            val response = characterRemoteDataSource.getMultipleCharactersByIds(ids)
            if (response.isSuccessful) {
                val characters = response.body().orEmpty()
                Resource.Success(characters)
            } else {
                Resource.Fail(response.message())
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


    suspend fun getCharacterById(resident: String): Resource<List<Character>> {
        val ids = resident.extractId()
        return try {
            val response = characterRemoteDataSource.getCharacterById(ids)
            if (response.isSuccessful) {

                val character = response.body()
                val characterList = if (character != null) listOf(character) else emptyList()
                Resource.Success(characterList)
            } else {
                Resource.Fail(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


    //Fav
    suspend fun insertCharacter(character: Character) {
        val favCharacter = CharacterFav(
            id = character.id,
            name = character.name,
            status = character.status,
            species = character.species,
            image = character.image,
        )
        characterLocalDataSource.insertCharacter(favCharacter)
    }

    suspend fun deleteCharacterById(characterId: Int) {
        characterLocalDataSource.deleteCharacterById(characterId)
    }

    fun getFavCharacters(): Flow<List<CharacterFav>> {
        return characterLocalDataSource.getFavCharacters()
    }

    suspend fun isFavorite(id: Int): Boolean {
        return characterLocalDataSource.isFavorite(id)
    }

    suspend fun deleteAll() {
        characterLocalDataSource.deleteAll()
    }


}
