package com.example.myweatherapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.data.ApiLocationService
import com.example.myweatherapp.data.ApiService
import com.example.myweatherapp.data.NetworkResponse
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing weather data, location input, temperature unit, and
 * error handling for the weather app.
 */
class WeatherViewModel: ViewModel() {
    private val api = ApiService.service
    private val geoApi = ApiLocationService.service2

    // Backing property to avoid state updates from other classes
    // to update state and send it to the flow, assign a new value to the value property of the MutableStateFlow class
    private val _weatherData = MutableStateFlow<NetworkResponse?>(null)
    val weatherData: StateFlow<NetworkResponse?> = _weatherData // maintain an observable mutable state

    // Temperature unit (0 = celsius, 1 = fahrenheit)
    private val _unit = MutableStateFlow(0)
    val unit: StateFlow<Int> = _unit

    /**
     * Sets the temperature unit and triggers fetchWeather function with the new unit.
     *
     * @param index 0 for Celsius, 1 for Fahrenheit
     */
    fun setUnit(index: Int) {
        _weatherData.value = NetworkResponse.Loading
        _unit.value = index
        fetchWeather()
    }

    // Location
    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> = _cityName

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Sets the location by name and triggers fetchWeather function.
     * Validates empty input.
     *
     * @param name The name of the city or country to search for.
     */
    fun setLocation(name: String) {
        if (name.isBlank()) {
            _errorMessage.value = "Please enter a city or country name."
            _weatherData.value = null
            return
        }
        _errorMessage.value = ""
        _cityName.value = name
        fetchWeather()
    }

    /**
     * Clears all weather-related state including location and error messages.
     */
    fun clear() {
        _weatherData.value = null
        _cityName.value = ""
        _errorMessage.value = ""
    }

    /**
     * Fetches weather data from API based on the city name and temperature unit.
     */
    private fun fetchWeather() {
        val temperatureUnit = if (_unit.value == 0) "celsius" else "fahrenheit"

        // starts a coroutine
        viewModelScope.launch {
            _weatherData.value = NetworkResponse.Loading // show loading

            try {
                val geoResponse = geoApi.getLocation(name = _cityName.value)
                val correctCityName = geoResponse.results?.firstOrNull() { it.name.equals(_cityName.value, ignoreCase = true) }

                if (correctCityName != null) {
                    _cityName.value = correctCityName.name

                    // fetch city's weather data
                    val weatherRes = api.getWeather(
                        temperatureUnit = temperatureUnit,
                        latitude = correctCityName.latitude,
                        longitude = correctCityName.longitude
                    )
                    _weatherData.value = NetworkResponse.Success(weatherRes)
                    _errorMessage.value = ""
                } else {
                    _weatherData.value = null
                    _errorMessage.value = "City or country not found. Please check the spelling and try again."
                }
            } catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error(
                    "Something went wrong. Please check your internet connection or try again.")
            }
        }
    }
}
