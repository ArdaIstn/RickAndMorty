package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.data.room.FavCharacterDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): FavCharacterDB {
        return Room.databaseBuilder(context, FavCharacterDB::class.java, "fav_character_db").build()
    }

    @Provides
    @Singleton
    fun provideFavCharacterDao(favCharacterDB: FavCharacterDB) = favCharacterDB.favCharacterDao()


}