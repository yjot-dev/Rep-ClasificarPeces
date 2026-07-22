package com.yjotdev.clasificarpeces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.clasificarpeces.databinding.ActivityMainBinding
import com.yjotdev.clasificarpeces.presentation.navigation.setupNavigation

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    internal lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura la IU de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Configura la navegación, toolbar y edge-to-edge
        setupNavigation()
    }
}