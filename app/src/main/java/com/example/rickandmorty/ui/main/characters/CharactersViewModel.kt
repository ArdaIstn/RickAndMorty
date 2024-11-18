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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _locations = MutableLiveData<List<Result>>()
    val locations: LiveData<List<Result>> get() = _locations

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    init {
        fetchLocations()
    }

    private fun fetchLocations() {
        viewModelScope.launch {
            when (val response = characterRepository.getResultList()) {
                is Resource.Success -> {
                    _locations.postValue(response.data)
                }

                is Resource.Fail -> {
                    // Hata durumunda hata mesajını gönderiyoruz
                    Log.e("CharactersViewModel", "Error fetching locations: ${response.message}")
                }

                is Resource.Error -> TODO()
            }

        }
    }

    fun fetchCharactersByLocation(residentList: List<String>) {
        viewModelScope.launch {
            // Eğer liste boşsa, hata mesajı gönder
            if (residentList.isEmpty()) {
                return@launch
            }

            // Eğer liste tek bir elemandan oluşuyorsa, getCharacterByLocation fonksiyonunu kullan
            if (residentList.size == 1) {
                // Tek bir eleman için getCharacterByLocation fonksiyonunu çağır
                val result = characterRepository.getCharacterByLocation(residentList.first())

                when (result) {
                    is Resource.Success -> {
                        // Karakter başarıyla alındı, veriyi post et
                        _characters.postValue(result.data)

                    }

                    is Resource.Fail -> {
                        // Hata mesajı al

                    }

                    is Resource.Error -> {
                        // İstisna mesajı al
                    }
                }
            } else {
                // Birden fazla eleman varsa, getCharactersByLocation fonksiyonunu kullan
                when (val result = characterRepository.getCharactersByLocation(residentList)) {
                    is Resource.Success -> {
                        // Karakterler başarıyla alındı
                        _characters.postValue(result.data)
                    }

                    is Resource.Fail -> {
                        // Hata mesajı al

                    }

                    is Resource.Error -> {
                        // İstisna mesajı al

                    }
                }
            }
        }
    }

}
