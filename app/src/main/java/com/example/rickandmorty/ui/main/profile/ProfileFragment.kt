package com.example.rickandmorty.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentProfileBinding
import com.example.rickandmorty.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSignOut.setOnClickListener {
                signOutOrNot()
            }
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        profileViewModel.profileState.observe(viewLifecycleOwner) { profileState ->
            binding.apply {
                profileState.user?.let {
                    tvEmail.text = it.email
                    tvNickname.text = it.nickname
                    tvPhoneNumber.text = it.phoneNumber
                }
                progressBarEmail.visibility =
                    if (profileState.isLoading) View.VISIBLE else View.GONE
                progressBarNickname.visibility =
                    if (profileState.isLoading) View.VISIBLE else View.GONE
                progressBarPhoneNumber.visibility =
                    if (profileState.isLoading) View.VISIBLE else View.GONE
            }

        }
    }

    private fun signOutOrNot() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        materialAlertDialogBuilder.setTitle("Sign Out")
        materialAlertDialogBuilder.setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") { _, _ ->
                profileViewModel.signOut().also {
                    Intent(requireContext(), LoginActivity::class.java).also {
                        startActivity(it)
                        requireActivity().finish()
                    }

                }
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


}