package com.example.rickandmorty.ui.login.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.data.repository.UserRepository

import javax.inject.Inject

@HiltViewModel
class SignUpFragmentViewModel @Inject constructor(
    private val usersRepo: UserRepository,
) : ViewModel() {

    private var _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState>
        get() = _signUpState

    fun signUp(
        eMail: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String,
    ) {
        viewModelScope.launch {
            val eMailError = validateEmail(eMail)
            val passwordError = validatePassword(password)
            val confirmPasswordError = validateConfirmPassword(password, confirmPassword)
            val nicknameError = validateNickname(nickname)
            val phoneNumberError = validatePhoneNumber(phoneNumber)

            if (eMailError == null && passwordError == null && confirmPasswordError == null && nicknameError == null && phoneNumberError == null) {
                // If validation passes, call repository's signUp method
                when (val response = usersRepo.signUp(eMail, password, nickname, phoneNumber)) {
                    is Resource.Success -> {
                        _signUpState.value = SignUpState(
                            isSignUp = true
                        )
                    }

                    is Resource.Fail -> {
                        _signUpState.value = SignUpState(failMessage = response.message)
                    }

                    is Resource.Error -> {
                        _signUpState.value = SignUpState(
                            errorMessage = response.throwable.message
                        )
                    }
                }
            } else {
                // If there are errors, update the error state for each field
                _signUpState.value = SignUpState(
                    eMailError = eMailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError,
                    nicknameError = nicknameError,
                    phoneNumberError = phoneNumberError
                )
            }
        }
    }

    private fun validateEmail(eMail: String): String? {
        return when {
            eMail.isEmpty() -> "E-posta adresi boş olamaz"
            !eMail.contains("@") -> "Geçerli bir e-posta adresi giriniz"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Şifre boş olamaz"
            else -> null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Şifreyi onaylayınız"
            confirmPassword != password -> "Şifreler eşleşmiyor"
            else -> null
        }
    }

    private fun validateNickname(nickname: String): String? {
        return if (nickname.isEmpty()) "Takma ad boş olamaz" else null
    }

    private fun validatePhoneNumber(phoneNumber: String): String? {
        return if (phoneNumber.isEmpty()) "Telefon numarası boş olamaz" else null
    }
}

data class SignUpState(
    val isSignUp: Boolean = false,
    val failMessage: String? = null,
    val errorMessage: String? = null,
    val eMailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nicknameError: String? = null,
    val phoneNumberError: String? = null
)
