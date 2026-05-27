package com.example.albummundial_pruebafinal.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerApiService {
    @GET("searchplayers.php")
    suspend fun searchPlayer(
        @Query("p") playerName: String
    ): PlayerResponse

    companion object {
        private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/3/"

        fun create(): PlayerApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlayerApiService::class.java)
        }
    }
}
