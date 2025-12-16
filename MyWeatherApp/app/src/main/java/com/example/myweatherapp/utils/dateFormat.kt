package com.example.myweatherapp.utils

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Formats a date string from "yyyy-MM-dd" to a format like "May 20".
 *
 * @param dateValue The original date string in format "yyyy-MM-dd".
 * @return A formatted date string in "MMM-d" format, or original if parsing fails.
 */
fun dateFormat(dateValue: String): String {
    // "2025-05-28" -> "May 28"
    return try {
        LocalDate.parse(dateValue).format(DateTimeFormatter.ofPattern("MMM d"))
    } catch (e: Exception) {
        Log.e("FormatDate", "Error parsing date: ${e.message}")
        dateValue
    }
}
