package com.example.myweatherapp.utils

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Formats a date string from "yyyy-MM-dd" to "MM-dd".
 *
 * @param date The original date string.
 * @return A formatted date string or original if parsing fails.
 */
fun formatForecastDate(date: String): String {
    // "2025-05-05" -> "05-05"
    return try {
        LocalDate.parse(date).format(DateTimeFormatter.ofPattern("MM-dd"))
    } catch (e: Exception) {
        Log.e("FormatDate", "Error parsing date: ${e.message}")
        date
    }
}
