package com.example.rickandmorty.ui.login.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentSignInBinding
import com.example.rickandmorty.ui.login.LoginActivity
import com.example.rickandmorty.ui.main.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val signInViewModel: SignInFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSignIn.setOnClickListener {
                val email = editTextEmailSignIn.text.toString()
                val password = editTextPasswordSignIn.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signInViewModel.signIn(email, password)
                } else {
                    Snackbar.make(binding.root, "Please fill in all fields", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        signInViewModel.signInState.observe(viewLifecycleOwner) {
            if (it.isSignIn) {
                Snackbar.make(binding.root, "Succesfully Login!", Snackbar.LENGTH_SHORT).show()
                Intent(context, MainActivity::class.java).apply {
                    startActivity(this)
                    requireActivity().finish()
                }
            }
            if (it.signInFailMessage != null) {
                Snackbar.make(binding.root, "User Not Found!", Snackbar.LENGTH_SHORT).show()
            }
            if (it.signInErrorMessage != null) {
                Snackbar.make(binding.root, "Network Error!!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}