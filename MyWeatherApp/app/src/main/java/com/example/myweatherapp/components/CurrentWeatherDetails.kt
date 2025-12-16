package com.example.myweatherapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays a weather-related detail with an icon, value, and label in a column.
 * Used for current conditions like rain, wind speed, or humidity.
 *
 * @param icon Resource ID of the icon to show.
 * @param value The value to display (e.g. "50%").
 * @param label A label for the data (e.g. "Rain").
 */
@Composable
fun CurrentWeatherDetails(icon: Int, value: String?, label: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Image(
            painter = painterResource(id = icon),
            contentDescription = "detail icon",
            modifier = Modifier
                .size(63.dp)
                .padding(bottom = 14.dp)
        )
        // Value
        Text(
            text = "$value",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        // Label
        Text(
            text = "$label",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}