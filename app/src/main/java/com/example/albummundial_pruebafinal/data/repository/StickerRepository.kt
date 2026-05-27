package com.example.albummundial_pruebafinal.data.repository

import com.example.albummundial_pruebafinal.data.local.StickerDao
import com.example.albummundial_pruebafinal.data.local.StickerEntity
import com.example.albummundial_pruebafinal.data.remote.PlayerApiService
import com.example.albummundial_pruebafinal.data.remote.PlayerResponse
import kotlinx.coroutines.flow.Flow

class StickerRepository(
    private val stickerDao: StickerDao,
    private val apiService: PlayerApiService
) {
    val allStickers: Flow<List<StickerEntity>> = stickerDao.getAllStickers()
    val obtainedStickers: Flow<List<StickerEntity>> = stickerDao.getObtainedStickers()
    val repeatedStickers: Flow<List<StickerEntity>> = stickerDao.getRepeatedStickers()

    suspend fun registerSticker(sticker: StickerEntity) {
        stickerDao.insertSticker(sticker)
    }

    suspend fun incrementSticker(id: String) {
        stickerDao.incrementQuantity(id)
    }

    suspend fun exchangeStickers(giveId: String, receiveId: String) {
        stickerDao.decrementQuantity(giveId)
        stickerDao.incrementQuantity(receiveId)
    }

    suspend fun searchPlayer(name: String): PlayerResponse {
        return apiService.searchPlayer(name)
    }
}
