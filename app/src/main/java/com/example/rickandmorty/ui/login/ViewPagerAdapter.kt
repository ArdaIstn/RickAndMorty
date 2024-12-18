package com.example.rickandmorty.ui.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmorty.ui.login.signin.SignInFragment
import com.example.rickandmorty.ui.login.signup.SignUpFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignInFragment()
            1 -> SignUpFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}