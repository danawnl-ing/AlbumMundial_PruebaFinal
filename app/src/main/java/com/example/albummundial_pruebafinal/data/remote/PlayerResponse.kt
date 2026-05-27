package com.example.albummundial_pruebafinal.data.remote

import com.google.gson.annotations.SerializedName

data class PlayerResponse(
    @SerializedName("player")
    val players: List<Player>?
)

data class Player(
    @SerializedName("idPlayer")
    val id: String,
    @SerializedName("strPlayer")
    val name: String,
    @SerializedName("strTeam")
    val team: String,
    @SerializedName("strCutout")
    val photoUrl: String?, // Foto sin fondo
    @SerializedName("strDescriptionEN")
    val description: String?
)
