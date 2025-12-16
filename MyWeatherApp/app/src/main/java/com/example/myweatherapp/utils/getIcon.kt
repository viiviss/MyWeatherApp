package com.example.myweatherapp.utils

import com.example.myweatherapp.R

/**
 * Maps a weather code to a drawable resource ID for the corresponding weather icon.
 *
 * @param weatherCode An integer representing the weather condition code.
 * @return The resource ID of the appropriate weather icon.
 */
fun getIcon(weatherCode: Int): Int {
    return when(weatherCode) {
        0 -> R.drawable.sun2
        1, 2, 3 -> R.drawable.partlycloudy2
        45, 48 -> R.drawable.fog2
        51, 53, 55, 61, 63, 65, 66, 67, 80, 81, 82 -> R.drawable.rain2
        71, 73, 75, 77, 85, 86 -> R.drawable.snowy2
        95, 96, 99 -> R.drawable.storm2
        else -> R.drawable.unknown
    }
}
