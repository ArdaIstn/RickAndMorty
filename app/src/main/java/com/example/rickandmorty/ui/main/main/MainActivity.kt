package com.example.rickandmorty.ui.main.main

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.common.gone
import com.example.rickandmorty.common.visible
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.ui.main.characters.CharactersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        hideOrShowBottomNav()

        binding.bottomNavigationView.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.characters_nav_graph -> {

                    val currentFragment =
                        navHostFragment.childFragmentManager.fragments.firstOrNull()

                    if (currentFragment is CharactersFragment) {
                        currentFragment.resetVerticalRecyclerView()
                    }
                }

            }
        }

    }

    private fun hideOrShowBottomNav() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom


            if (keypadHeight > screenHeight * 0.15) {
                binding.bottomNavigationView.gone()
            } else {
                binding.bottomNavigationView.visible()
            }
        }
    }

    private fun setupBottomNav() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}