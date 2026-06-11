package com.example.albummundial_pruebafinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.albummundial_pruebafinal.data.local.AppDatabase
import com.example.albummundial_pruebafinal.data.remote.PlayerApiService
import com.example.albummundial_pruebafinal.data.repository.StickerRepository
import com.example.albummundial_pruebafinal.ui.screens.MainScreen
import com.example.albummundial_pruebafinal.ui.theme.AlbumMundial_PruebaFinalTheme
import com.example.albummundial_pruebafinal.ui.viewmodels.StickerViewModel
import com.example.albummundial_pruebafinal.ui.viewmodels.StickerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización manual de la arquitectura (DI simple)
        val database = AppDatabase.getDatabase(this)
        val apiService = PlayerApiService.create()
        val repository = StickerRepository(database.stickerDao(), apiService)

        val viewModel: StickerViewModel by viewModels {
            StickerViewModelFactory(repository)
        }

        viewModel.loadInitialStickers()

        enableEdgeToEdge()
        setContent {
            AlbumMundial_PruebaFinalTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
