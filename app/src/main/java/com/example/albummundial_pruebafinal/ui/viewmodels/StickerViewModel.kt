package com.example.albummundial_pruebafinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.albummundial_pruebafinal.data.local.StickerEntity
import com.example.albummundial_pruebafinal.data.remote.Player
import com.example.albummundial_pruebafinal.data.repository.StickerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StickerViewModel(
    private val repository: StickerRepository
) : ViewModel() {

    val allStickers = repository.allStickers
    val obtainedStickers = repository.obtainedStickers
    val pendingStickers = repository.pendingStickers
    val repeatedStickers = repository.repeatedStickers

    private val _searchResults = MutableStateFlow<List<Player>>(emptyList())
    val searchResults: StateFlow<List<Player>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun registerSticker(
        id: String,
        name: String,
        team: String
    ) {
        viewModelScope.launch {
            repository.registerSticker(
                StickerEntity(
                    id = id,
                    name = name,
                    team = team,
                    isObtained = true,
                    quantity = 1
                )
            )
        }
    }

    fun obtainSticker(sticker: StickerEntity) {
        viewModelScope.launch {
            repository.registerSticker(
                sticker.copy(
                    isObtained = true,
                    quantity = 1
                )
            )
        }
    }

    fun addRepeated(id: String) {
        viewModelScope.launch {
            repository.incrementSticker(id)
        }
    }

    fun exchangeSticker(
        giveId: String,
        receiveId: String
    ) {
        viewModelScope.launch {
            repository.exchangeStickers(
                giveId,
                receiveId
            )
        }
    }

    fun searchPlayer(name: String) {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                val response = repository.searchPlayer(name)
                _searchResults.value =
                    response.players ?: emptyList()

            } catch (e: Exception) {

                _searchResults.value = emptyList()

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun loadInitialStickers() {
        viewModelScope.launch {

            val stickers = listOf(

                StickerEntity(
                    id = "ARG1",
                    name = "Lionel Messi",
                    team = "Argentina",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "ARG2",
                    name = "Julián Álvarez",
                    team = "Argentina",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "BRA1",
                    name = "Vinicius Jr",
                    team = "Brasil",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "BRA2",
                    name = "Rodrygo",
                    team = "Brasil",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "COL1",
                    name = "Luis Díaz",
                    team = "Colombia",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "COL2",
                    name = "James Rodríguez",
                    team = "Colombia",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "FRA1",
                    name = "Kylian Mbappé",
                    team = "Francia",
                    isObtained = false,
                    quantity = 0
                ),

                StickerEntity(
                    id = "POR1",
                    name = "Cristiano Ronaldo",
                    team = "Portugal",
                    isObtained = false,
                    quantity = 0
                )
            )

            stickers.forEach {
                repository.registerSticker(it)
            }
        }
    }
}

class StickerViewModelFactory(
    private val repository: StickerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(StickerViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return StickerViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}