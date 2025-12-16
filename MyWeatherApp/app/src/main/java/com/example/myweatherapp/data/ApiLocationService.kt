package com.example.myweatherapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for geocoding requests using Open-Meteo's geocoding API.
 */
interface ApiLocationService {
    /**
     * Fetches geographic coordinates for a given location name.
     *
     * @param name Name of the location.
     * @return LocationResponse containing coordinates and location details.
     */
    @GET("v1/search")
    suspend fun getLocation(
        @Query("name") name : String
    ) : LocationResponse

    companion object {
        /**
         * A singleton Retrofit instance of the geocoding API service.
         */
        val service2: ApiLocationService by lazy {
            Retrofit.Builder()
                .baseUrl("https://geocoding-api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiLocationService::class.java)
        }
    }
}