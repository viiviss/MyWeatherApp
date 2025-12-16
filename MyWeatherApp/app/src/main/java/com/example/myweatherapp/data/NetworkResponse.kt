package com.example.myweatherapp.data

/**
 * Represents the state of a weather data network request.
 */

sealed class NetworkResponse {
    /**
     * Indicates that the request is currently loading.
     */
    data object Loading: NetworkResponse()

    /**
     * Data was successfully fetched.
     *
     * @param data The weather data retrieved.
     */
    data class Success(val data: WeatherResponse) : NetworkResponse()

    /**
     * An error occurred while fething data.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : NetworkResponse()
}
// to handle different states (loading, error, success) of network requests
// a sealed class allows to define a restricted hierarchy of subclasses (must be declared within the same file)
// useful for situations where you want to represent a finite number of states

