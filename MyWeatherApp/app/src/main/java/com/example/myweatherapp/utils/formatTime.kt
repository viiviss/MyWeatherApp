package com.example.myweatherapp.utils

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Extracts and returns the time in "HH-mm" format from a date-time string.
 *
 * @param date A date-time string in the format "yyyy-MM-dd'T'HH:mm".
 * @return A string showing only the time, or original if parsing fails.
 */
fun formatTime(date: String): String {
    // "2025-10-20T14:00" -> "14:00"
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val time = LocalDateTime.parse(date, formatter)
        time.format(DateTimeFormatter.ofPattern("HH:mm"))

    } catch (e: Exception) {
        Log.e("FormatDate", "Error parsing date: ${e.message}")
        date
    }
}
