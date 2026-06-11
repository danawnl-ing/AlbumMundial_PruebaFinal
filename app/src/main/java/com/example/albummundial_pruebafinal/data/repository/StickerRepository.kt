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

    val allStickers: Flow<List<StickerEntity>> =
        stickerDao.getAllStickers()

    // Consulta de láminas obtenidas
    val obtainedStickers: Flow<List<StickerEntity>> =
        stickerDao.getObtainedStickers()

    // Consulta de láminas pendientes
    val pendingStickers: Flow<List<StickerEntity>> =
        stickerDao.getPendingStickers()

    val repeatedStickers: Flow<List<StickerEntity>> =
        stickerDao.getRepeatedStickers()

    suspend fun registerSticker(sticker: StickerEntity) {
        stickerDao.insertSticker(sticker)
    }

    suspend fun incrementSticker(id: String) {
        stickerDao.incrementQuantity(id)
    }

    //MODIFICACIÓN:
    suspend fun exchangeStickers(giveId: String, receiveId: String) {

        stickerDao.decrementQuantity(giveId)

        if (receiveId.isNotBlank()) {
            stickerDao.incrementQuantity(receiveId)
        }
    }

    suspend fun searchPlayer(name: String): PlayerResponse {
        return apiService.searchPlayer(name)
    }
}