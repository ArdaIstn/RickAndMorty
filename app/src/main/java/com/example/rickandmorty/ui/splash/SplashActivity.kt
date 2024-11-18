package com.example.rickandmorty.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.R
import com.example.rickandmorty.common.Constants
import com.example.rickandmorty.common.Constants.FIRST_LAUNCH_KEY
import com.example.rickandmorty.common.Constants.PREFS_NAME
import com.example.rickandmorty.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true)

        // Karşılama metnini ayarla
        binding.tvWelcome.text = if (isFirstLaunch) "Welcome!" else "Hello!"

        // İlk açılışsa, değerini false olarak güncelle
        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply()
        }

        // 2 saniye sonra ana ekrana geç
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
