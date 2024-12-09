package com.example.rickandmorty.di

import com.example.rickandmorty.data.datasource.local.CharacterLocalDataSource
import com.example.rickandmorty.data.datasource.remote.CharacterRemoteDataSource
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.retrofit.CharacterApi
import com.example.rickandmorty.data.room.FavCharacterDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(characterApi: CharacterApi): CharacterRemoteDataSource {
        return CharacterRemoteDataSource(characterApi)
    }


    @Provides
    @Singleton
    fun provideLocalDataSource(favCharacterDao: FavCharacterDAO): CharacterLocalDataSource {
        return CharacterLocalDataSource(favCharacterDao)
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: CharacterRemoteDataSource,
        localDataSource: CharacterLocalDataSource
    ): CharacterRepository {
        return CharacterRepository(remoteDataSource, localDataSource)
    }


}