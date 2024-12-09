package com.example.rickandmorty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.model.CharacterFav


@Database(entities = [CharacterFav::class], version = 1, exportSchema = false)
abstract class FavCharacterDB : RoomDatabase() {

    abstract fun favCharacterDao(): FavCharacterDAO

}