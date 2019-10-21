package com.ratushny.wallsgeyser.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {

    @GET(".")
    suspend fun getPixabayDataService(@Query("key") key: String, @Query("category") category: String): PixabayResponse

    companion object Factory {
        fun create(): PixabayService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pixabay.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(PixabayService::class.java)
        }
    }
}