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

class StickerViewModel(private val repository: StickerRepository) : ViewModel() {

    val allStickers = repository.allStickers
    val obtainedStickers = repository.obtainedStickers
    val repeatedStickers = repository.repeatedStickers

    private val _searchResults = MutableStateFlow<List<Player>>(emptyList())
    val searchResults: StateFlow<List<Player>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun registerSticker(id: String, name: String, team: String) {
        viewModelScope.launch {
            repository.registerSticker(StickerEntity(id, name, team, isObtained = true, quantity = 1))
        }
    }

    fun addRepeated(id: String) {
        viewModelScope.launch {
            repository.incrementSticker(id)
        }
    }

    fun exchangeSticker(giveId: String, receiveId: String) {
        viewModelScope.launch {
            repository.exchangeStickers(giveId, receiveId)
        }
    }

    fun searchPlayer(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.searchPlayer(name)
                _searchResults.value = response.players ?: emptyList()
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class StickerViewModelFactory(private val repository: StickerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StickerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
