package com.example.rickandmorty.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.CharacterFav
import kotlinx.coroutines.flow.Flow

@Dao
interface FavCharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterFav)

    @Query("DELETE FROM character_fav WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)

    @Query("SELECT * FROM character_fav")
    fun getFavCharacters(): Flow<List<CharacterFav>>

    @Query("SELECT EXISTS(SELECT 1 FROM character_fav WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("DELETE FROM character_fav")
    suspend fun deleteAll()

}