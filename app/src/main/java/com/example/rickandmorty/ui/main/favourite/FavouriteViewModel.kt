package com.example.rickandmorty.ui.main.favourite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.CharacterFav
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FavouriteViewModel @Inject constructor(private val characterRepository: CharacterRepository) :
    ViewModel() {
    private val _favCharacters = MutableLiveData<List<CharacterFav>>()
    val favCharacters: LiveData<List<CharacterFav>> = _favCharacters

    init {
        getFavCharacters()
    }

    private fun getFavCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            val favCharacters = characterRepository.getFavCharacters()
            withContext(Dispatchers.Main) {
                favCharacters.collect {
                    _favCharacters.value = it
                }
            }
        }
    }

    fun deleteCharacterById(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.deleteCharacterById(characterId)
        }

    }
}