package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_fav")
data class CharacterFav(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val status: String?,
    val species: String?,
    val image: String?,
)
