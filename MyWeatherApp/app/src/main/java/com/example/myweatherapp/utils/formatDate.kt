package com.example.myweatherapp.utils

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Formats a full date-time string to display the day of the week and date.
 *
 * Example: "2025-05-05T15:00" -> "Tuesday | 2025-05-05"
 *
 * @param date The date-time string in format "yyyy-MM-dd'T'HH:mm".
 * @return A formatted date string showing day of the week and date, or original if parsing fails.
 */
fun formatDate(date: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val dateTime = LocalDateTime.parse(date, formatter)
        val currentDate = dateTime.toLocalDate()

        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        "$dayOfWeek | $currentDate"
    } catch (e: Exception) {
        Log.e("FormatDate", "Error parsing date: ${e.message}")
        date
    }
}
