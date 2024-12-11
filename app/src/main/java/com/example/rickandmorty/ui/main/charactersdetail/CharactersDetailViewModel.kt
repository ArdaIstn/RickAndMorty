package com.example.rickandmorty.ui.main.charactersdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CharactersDetailViewModel @Inject constructor(private val characterRepository: CharacterRepository) :
    ViewModel() {
    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> get() = _character

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    fun setCharacter(character: Character) {
        _character.value = character
    }

    fun insertFavCharacter(character: Character) {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.insertCharacter(character)
            withContext(Dispatchers.Main) {
                _isFavourite.postValue(true)
            }  // Favori durumunu güncelle
        }
    }

    fun isFavourite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = characterRepository.isFavorite(id)
            withContext(Dispatchers.Main) {
                _isFavourite.postValue(isFav)
            }
        }
    }


}