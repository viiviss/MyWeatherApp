package com.example.myweatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myweatherapp.components.DetailsCard
import com.example.myweatherapp.data.NetworkResponse
import com.example.myweatherapp.R
import com.example.myweatherapp.view.WeatherViewModel
import com.example.myweatherapp.utils.getIcon
import com.example.myweatherapp.utils.formatTime
import com.example.myweatherapp.utils.dateFormat
/**
 * Displays detailed weather information for a selected day.
 *
 * This composable function shows:
 * - Selected date, temperature, and weather icon
 * - Hourly temperature forecast for 24 hours
 * - Sunrise, sunset, wind speed, minimum temperature, rain probability, and humidity
 *
 * @param navController Used for navigation between screens.
 * @param index Index of the selected day.
 * @param date Date representing the selected day.
 * @param tempUnit Temperature unit.
 * @param viewModel Shared ViewModel that provides weather data and state.
 */
@Composable
fun DetailScreen(
    navController: NavController,
    index: Int,
    date: String,
    tempUnit: String,
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.weatherData.collectAsState()

    when(val result = uiState) {
        null -> {}
        // Loading state
        is NetworkResponse.Loading -> {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // Error state
        is NetworkResponse.Error -> {
            val message = result.message
            Text(
                text = message,
                fontSize = 18.sp,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
        // Success state
        is NetworkResponse.Success -> {
            val weatherData = result.data

            val hour = "${date}T15:00" // add time 15:00 to date
            val indexOfHour = weatherData.hourly.time.indexOf(hour) // find index

            weatherData.let {
                val formattedDate = dateFormat(date)
                val maxTemp = it.daily.temperature_2m_max.getOrNull(index) ?: 0.0
                val minTemp = it.daily.temperature_2m_min.getOrNull(index) ?: 0.0
                val icon = getIcon(it.daily.weather_code.getOrNull(index) ?: -1)
                val sunrise = formatTime(it.daily.sunrise.getOrNull(index) ?: "")
                val sunset = formatTime(it.daily.sunset.getOrNull(index) ?: "")
                val wind = it.daily.wind_speed_10m_max.getOrNull(index) ?: 0.0
                val humidity = it.hourly.relative_humidity_2m.getOrNull(indexOfHour) ?: 0.0 // 15:00 humidity
                val rain = it.hourly.precipitation_probability.getOrNull(indexOfHour) ?: 0.0 // 15:00 rain

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    item {
                        // Date
                        Text(
                            text = formattedDate,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 25.dp, top = 44.dp)
                        )
                    }
                    item {
                        // Icon and temperature
                        Card(
                            Modifier.padding(bottom = 20.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(25.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = "detailScreen icon",
                                    modifier = Modifier
                                        .size(80.dp)
                                )
                                Text(
                                    text = "${maxTemp}${tempUnit}",
                                    fontSize = 42.sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    // Display temperature 00:00 -> 23:00
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // date = "2025-06-06" -> "2025-06-06T00:00"
                            val findDate = "${date}T00:00"
                            val startIndex = weatherData.hourly.time.indexOf(findDate)

                            if (startIndex != -1) {
                                items(24) { i ->
                                    val indexOfTime = startIndex + i
                                    val temp = weatherData.hourly.temperature_2m.getOrNull(indexOfTime) ?: 0.0 // avoid indexOutOfBounds
                                    val time = weatherData.hourly.time.getOrNull(indexOfTime)?.takeLast(5) ?: "${i}:00"
                                    val weatherIcon = getIcon(weatherData.hourly.weather_code.getOrNull(indexOfTime) ?: -1)

                                    Card(
                                        shape = RoundedCornerShape(18.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(18.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            // Time
                                            Text(
                                                text = time,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(bottom = 5.dp)
                                                )
                                            // Icon
                                            Image(
                                                painter = painterResource(id = weatherIcon),
                                                contentDescription = "weather icon",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .padding(bottom = 5.dp)
                                            )
                                            // Temperature
                                            Text(
                                                text = "$temp°C",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        // Detail cards
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Sunrise and Sunset
                            Row {
                                DetailsCard(
                                    cardName = "Sunrise",
                                    value = sunrise,
                                    icon = R.drawable.sunrise1
                                )
                                DetailsCard(
                                    cardName = "Sunset",
                                    value = sunset,
                                    icon = R.drawable.sunset1
                                )
                            }
                            // Wind speed and min temperature
                            Row {
                                DetailsCard(
                                    cardName = "Wind Speed",
                                    value = "$wind m/s",
                                    icon = R.drawable.wind
                                )
                                DetailsCard(
                                    cardName = "Min temperature",
                                    value = "${minTemp}${tempUnit}",
                                    icon = R.drawable.mintemperature
                                )
                            }
                            // Rain and humidity
                            Row {
                                DetailsCard(
                                    cardName = "Rain",
                                    value = "${rain}%",
                                    icon = R.drawable.umbrella1
                                )
                                DetailsCard(
                                    cardName = "Humidity",
                                    value = "${humidity}%",
                                    icon = R.drawable.humidity
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
