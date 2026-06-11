package com.example.albummundial_pruebafinal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StickerDao {

    @Query("SELECT * FROM stickers")
    fun getAllStickers(): Flow<List<StickerEntity>>

    // Láminas obtenidas
    @Query("SELECT * FROM stickers WHERE isObtained = 1")
    fun getObtainedStickers(): Flow<List<StickerEntity>>

    // Láminas pendientes
    @Query("SELECT * FROM stickers WHERE isObtained = 0")
    fun getPendingStickers(): Flow<List<StickerEntity>>

    @Query("SELECT * FROM stickers WHERE quantity > 1")
    fun getRepeatedStickers(): Flow<List<StickerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSticker(sticker: StickerEntity)

    @Update
    suspend fun updateSticker(sticker: StickerEntity)

    @Query("UPDATE stickers SET quantity = quantity + 1, isObtained = 1 WHERE id = :id")
    suspend fun incrementQuantity(id: String)

    @Query("UPDATE stickers SET quantity = CASE WHEN quantity > 0 THEN quantity - 1 ELSE 0 END WHERE id = :id")
    suspend fun decrementQuantity(id: String)
}
