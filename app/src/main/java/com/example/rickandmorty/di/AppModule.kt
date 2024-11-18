package com.example.rickandmorty.di

import com.example.rickandmorty.data.datasource.CharacterDataSource
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.retrofit.CharacterApi
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
    fun provideDataSource(characterApi: CharacterApi): CharacterDataSource {
        return CharacterDataSource(characterApi)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: CharacterDataSource): CharacterRepository {
        return CharacterRepository(dataSource)
    }


}