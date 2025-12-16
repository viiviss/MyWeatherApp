package com.example.myweatherapp.data
// these classes represent the JSON data we expect to receive from the API

/**
 * Response model for weather data.
 *
 * Contains current, hourly, and daily weather information.
 */
data class WeatherResponse(
    val current_weather: CurrentWeather,
    val hourly: Hourly,
    val daily: Daily
)

/**
 * Current weather data.
 */
data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val weatherCode: Int,
    val time: String
)

/**
 * Hourly weather forecast data.
 */
data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Double>,
    val wind_speed_10m: List<Double>,
    val precipitation_probability: List<Double>,
    val weather_code: List<Int>
)

/**
 * Daily weather forecast data.
 */
data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val weather_code: List<Int>,
    val wind_speed_10m_max: List<Double>
)

