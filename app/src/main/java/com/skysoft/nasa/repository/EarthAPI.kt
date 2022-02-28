package com.skysoft.nasa.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthAPI {
    @GET("planetary/earth/assets")
    fun getPictureOfEarth(
        @Query("api_key") apiKey: String,
        @Query("date") date: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<EarthResponse>
}