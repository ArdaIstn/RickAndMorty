package com.example.rickandmorty.ui.main.charactersdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel


class CharactersDetailViewModel : ViewModel() {
    private val _character = MutableLiveData<Character>()
    val character: MutableLiveData<Character> get() = _character

    fun setCharacter(character: Character) {
        _character.value = character
    }


}