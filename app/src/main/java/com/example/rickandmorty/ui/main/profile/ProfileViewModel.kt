package com.example.rickandmorty.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.data.model.UserModel
import com.example.rickandmorty.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//Repository'den gidip firestore'da tutulan verileri alıp ui'da gösterecek mekanizmayı kur.
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private var _profileState = MutableLiveData(ProfileState(isLoading = true))
    val profileState = _profileState

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            when (val response = userRepository.getUserInfo()) {
                is Resource.Success -> {
                    val user = response.data
                    _profileState.value = ProfileState(
                        user = user, isLoading = false
                    )

                }

                is Resource.Fail -> {
                    _profileState.value = ProfileState(
                        failMessage = response.message
                    )


                }

                is Resource.Error -> {
                    _profileState.value = ProfileState(
                        errorMessage = response.throwable.message
                    )

                }
            }
        }
    }

    fun signOut() {
        userRepository.signOut()
    }
}

data class ProfileState(
    val isLoading: Boolean = false,
    val user: UserModel? = null,
    val errorMessage: String? = null,
    val failMessage: String? = null
)