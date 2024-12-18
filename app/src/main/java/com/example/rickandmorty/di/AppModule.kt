package com.example.rickandmorty.di

import com.example.rickandmorty.data.datasource.local.CharacterLocalDataSource
import com.example.rickandmorty.data.datasource.remote.CharacterRemoteDataSource
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.UserRepository
import com.example.rickandmorty.data.retrofit.CharacterApi
import com.example.rickandmorty.data.room.FavCharacterDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideCharacterRepository(
        remoteDataSource: CharacterRemoteDataSource, localDataSource: CharacterLocalDataSource
    ): CharacterRepository {
        return CharacterRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun providesUserRepository(
        firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepository(firebaseAuth, firestore)
    }


}