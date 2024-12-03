package com.example.rickandmorty.ui.main.characters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterState())
    val uiState: StateFlow<CharacterState> get() = _uiState

    init {
        fetchLocations()
    }

    private fun fetchLocations() {
        viewModelScope.launch {

            when (val response = characterRepository.getResultList()) {
                is Resource.Success -> {
                    // Başarılı durumda State'i güncelliyoruz
                    _uiState.value = _uiState.value.copy(
                        locations = response.data, isEmpty = response.data.isEmpty()
                    )
                }

                is Resource.Fail -> {
                    // Hata durumunda failMessage ile güncelleme yapıyoruz
                    _uiState.value = _uiState.value.copy(
                        failMessage = response.message
                    )
                }

                is Resource.Error -> {
                    // Hata mesajını direkt olarak UI'ye gönderiyoruz
                    _uiState.value = _uiState.value.copy(
                        failMessage = response.throwable.message
                    )
                }
            }
        }
    }


    fun fetchCharactersByLocation(residentList: List<String>) {
        viewModelScope.launch {
            if (residentList.isEmpty()) {
                // Liste boşsa UI'yi güncelliyoruz
                _uiState.value = _uiState.value.copy(
                    characters = emptyList(), isEmpty = true
                )
                return@launch
            }


            val result = if (residentList.size == 1) {
                characterRepository.getCharacterByLocation(residentList.first())
            } else {
                characterRepository.getCharactersByLocation(residentList)
            }

            when (result) {
                is Resource.Success -> {
                    // Başarılı durumda karakterleri UI'ye gönderiyoruz
                    _uiState.value = _uiState.value.copy(
                        characters = result.data, isEmpty = result.data.isEmpty()
                    )
                }

                is Resource.Fail -> {
                    // Hata durumunda failMessage ile güncelliyoruz
                    _uiState.value = _uiState.value.copy(
                        failMessage = result.message
                    )
                }

                is Resource.Error -> {
                    // Hata mesajını UI'ye gönderiyoruz
                    _uiState.value = _uiState.value.copy(
                        failMessage = result.throwable.message
                    )
                }
            }
        }
    }

    data class CharacterState(
        val locations: List<Result>? = null,
        val characters: List<Character>? = null,
        var isEmpty: Boolean = false,
        val failMessage: String? = null
    )
}

