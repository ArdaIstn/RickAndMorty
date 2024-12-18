package com.example.rickandmorty.ui.login.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _signInState = MutableLiveData<SignInState>()
    val signInState: MutableLiveData<SignInState> get() = _signInState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            when (val response = userRepository.signIn(email, password)) {
                is Resource.Success -> {
                    _signInState.value = SignInState(isSignIn = true)

                }

                is Resource.Fail -> {
                    _signInState.value = SignInState(signInFailMessage = response.message)

                }

                is Resource.Error -> {
                    _signInState.value =
                        SignInState(signInErrorMessage = response.throwable.message)

                }
            }
        }
    }
}

data class SignInState(
    val isSignIn: Boolean = false,
    val signInFailMessage: String? = null,
    val signInErrorMessage: String? = null
)