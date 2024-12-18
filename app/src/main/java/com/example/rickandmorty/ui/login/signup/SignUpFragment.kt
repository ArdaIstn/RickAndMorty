package com.example.rickandmorty.ui.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.rickandmorty.databinding.FragmentSignUpBinding
import com.example.rickandmorty.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {
            btnSignUp.setOnClickListener {
                signUpViewModel.signUp(
                    eMail = editTextEmailSignUp.text.toString(),
                    password = editTextPasswordSignUp.text.toString(),
                    confirmPassword = editTextConfirmPassword.text.toString(),
                    nickname = editTextNickname.text.toString(),
                    phoneNumber = editTextPhoneNumber.text.toString()
                )
            }
            setUpObservers()
        }
        return binding.root


    }

    private fun setUpObservers() {
        signUpViewModel.signUpState.observe(viewLifecycleOwner) { state ->
            with(state) {
                // Başarı durumu
                if (isSignUp) navigateToSignInFragment()

                // Snackbar üzerinden genel mesajlar
                failMessage?.let { showSnackbar(it) }
                errorMessage?.let { showSnackbar(it) }
                // Input alanı hatalarını ayarla
                setFieldError(binding.editTextEmailSignUp, eMailError)
                setFieldError(binding.editTextPasswordSignUp, passwordError)
                setFieldError(binding.editTextConfirmPassword, confirmPasswordError)
                setFieldError(binding.editTextNickname, nicknameError)
                setFieldError(binding.editTextPhoneNumber, phoneNumberError)
            }
        }
    }

    private fun navigateToSignInFragment() {
        (requireActivity() as? LoginActivity)?.navigateToPage(0)
        clearTextFields()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setFieldError(textInputEditText: TextInputEditText, error: String?) {
        textInputEditText.error = error
    }

    private fun clearTextFields() {
        binding.editTextEmailSignUp.text?.clear()
        binding.editTextPasswordSignUp.text?.clear()
        binding.editTextConfirmPassword.text?.clear()
        binding.editTextNickname.text?.clear()
        binding.editTextPhoneNumber.text?.clear()
    }

}