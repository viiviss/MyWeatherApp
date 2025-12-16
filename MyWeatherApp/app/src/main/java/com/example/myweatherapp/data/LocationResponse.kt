package com.example.myweatherapp.data

/**
 * Response model for geocoding API.
 *
 * @property results List of matched geocoding results, or null if none found.
 */
data class LocationResponse (
    val results: List<GeocodingData>?
)

/**
 * Geocoding result containing a location name and its coordinates.
 */
data class GeocodingData(
    val name: String,
    val latitude: Double,
    val longitude: Double
)
