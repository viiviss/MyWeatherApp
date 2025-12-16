package com.example.myweatherapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for retrieving weather data from Open-Meteo's API.
 */
interface ApiService {
    /**
     * Retrieves weather forecast data for a specific location.
     *
     * @param latitude Latitude coordinate of the location.
     * @param longitude Longitude coordinate of the location.
     * @param currentWeather A boolean whether to include current weather.
     * @param hourly Hourly weather parameters.
     * @param daily Daily weather parameters.
     * @param timezone Timezone to align the data to.
     * @param temperatureUnit Temperature unit.
     * @param windSpeedUnit Wind speed unit.
     * @return WeatherResponse containing current, hourly, and daily data.
     */
    // getWeather will fetch the weather data
    // its marked with "suspend" bc it performs a network request and should be called from a coroutine
    // inside it we need to mention the parameters
    @GET("v1/forecast")
    // marked with "suspend" bc it performs a network request and should be called from a coroutine
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,precipitation_probability,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,sunrise,sunset,weather_code,wind_speed_10m_max",
        @Query("timezone") timezone: String = "auto",
        @Query("temperature_unit") temperatureUnit: String = "celsius", // default
        @Query("wind_speed_unit") windSpeedUnit: String = "ms"
    ): WeatherResponse

    // members inside the companion object can be accessed using the class name without creating an instance of the class
    companion object {
        /**
         * A singleton Retrofit instance for accessing the weather API.
         */
        // creates and configures a retrofit instance to interact with the open-meteo API
        val service: ApiService by lazy {
            // build the retrofit instance
            Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                // this configures retrofit to use Json converter for Json serialization and deserialization
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}