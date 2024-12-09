package com.example.rickandmorty.data.datasource.local

import com.example.rickandmorty.data.model.CharacterFav
import com.example.rickandmorty.data.room.FavCharacterDAO
import kotlinx.coroutines.flow.Flow

class CharacterLocalDataSource(private val favCharacterDAO: FavCharacterDAO) {
    suspend fun insertCharacter(character: CharacterFav) =
        favCharacterDAO.insertCharacter(character)

    suspend fun deleteCharacterById(characterId: Int) =
        favCharacterDAO.deleteCharacterById(characterId)

     fun getFavCharacters(): Flow<List<CharacterFav>> = favCharacterDAO.getFavCharacters()

    suspend fun isFavorite(id: Int): Boolean = favCharacterDAO.isFavorite(id)


}