package com.example.albummundial_pruebafinal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class StickerEntity(
    @PrimaryKey val id: String, // Código de la lámina (ej: "ARG10")
    val name: String,
    val team: String,
    val isObtained: Boolean = false,
    val quantity: Int = 0 // Cantidad total (1 si es obtenida, >1 si es repetida)
)
