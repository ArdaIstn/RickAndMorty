package com.example.rickandmorty.ui.main.characters

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

    private val _uiState = MutableStateFlow(CharacterState(isLoading = true))
    val uiState: StateFlow<CharacterState> get() = _uiState

    private var originalCharacterList: List<Character> = emptyList()
    private var currentCharacterList: List<Character> = emptyList()


    init {
        getLocations()
    }

    private fun getLocations() {
        viewModelScope.launch {
            when (val response = characterRepository.getLocations()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        locations = response.data, isEmpty = response.data.isEmpty()
                    )
                }

                is Resource.Fail -> {
                    _uiState.value = _uiState.value.copy(
                        failMessage = response.message
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        failMessage = response.throwable.message
                    )
                }
            }
        }
    }

    fun getCharacter(residentList: List<String>) {
        viewModelScope.launch {
            if (residentList.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    characters = emptyList(), isEmpty = true, isLoading = false
                )
                originalCharacterList = emptyList()
                currentCharacterList = emptyList()
                return@launch
            }
            val result = if (residentList.size == 1) {
                characterRepository.getCharacterById(residentList.first())
            } else {
                characterRepository.getMultipleCharactersByIds(residentList)
            }

            when (result) {
                is Resource.Success -> {
                    originalCharacterList = result.data
                    currentCharacterList = result.data
                    _uiState.value = _uiState.value.copy(
                        characters = result.data, isEmpty = result.data.isEmpty(), isLoading = false
                    )
                }

                is Resource.Fail -> {
                    _uiState.value = _uiState.value.copy(failMessage = result.message)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(failMessage = result.throwable.message)
                }
            }
        }
    }

    fun filterCharacters(query: String) {
        val filteredList = if (query.isEmpty()) {
            currentCharacterList
        } else {
            currentCharacterList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.value =
            _uiState.value.copy(characters = filteredList, isEmpty = filteredList.isEmpty())
    }

    fun filterCharactersByStatus(status: String) {

        currentCharacterList = if (status.isEmpty()) {
            originalCharacterList
        } else {
            originalCharacterList.filter {
                it.status.equals(status, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(
            characters = currentCharacterList, isEmpty = currentCharacterList.isEmpty()
        )
    }

    data class CharacterState(
        val locations: List<Result>? = null,
        val characters: List<Character>? = null,
        var isEmpty: Boolean = false,
        val failMessage: String? = null,
        val isLoading: Boolean = false,
    )
}
