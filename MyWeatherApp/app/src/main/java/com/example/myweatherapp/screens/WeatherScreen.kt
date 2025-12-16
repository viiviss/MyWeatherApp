package com.example.myweatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myweatherapp.components.CurrentWeatherDetails
import com.example.myweatherapp.R
import com.example.myweatherapp.components.SegmentedButton
import com.example.myweatherapp.components.ThemeSwitcher
import com.example.myweatherapp.data.WeatherResponse
import com.example.myweatherapp.view.WeatherViewModel
import com.example.myweatherapp.utils.getIcon
import com.example.myweatherapp.utils.formatDate
import com.example.myweatherapp.utils.formatForecastDate
/**
 * Displays the main weather screen with current weather and 7-day forecast.
 *
 * This composable function shows:
 * - The selected city and current date
 * - Current weather details
 * - Toggle for light/dark mode and temperature unit (°C/°F)
 * - 7-day forecast
 *
 * @param navController Used for navigating to other screens.
 * @param viewModel Provides weather data and state.
 * @param weatherData The weather data from the API.
 * @param darkTheme A boolean indicating whether the dark theme is enabled.
 * @param onThemeToggle A callback function to toggle theme.
 */
@Composable
fun WeatherScreen(
    navController: NavController,
    weatherData: WeatherResponse,
    viewModel: WeatherViewModel,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val city by viewModel.cityName.collectAsState()
    val formattedDate = formatDate(weatherData.current_weather.time) // "2025-05-05T15:00" -> "Monday | 2025-05-05"
    val selectedUnit by viewModel.unit.collectAsState() // for SegmentedButton (°C/°F)
    val tempUnit = if (selectedUnit == 0) "°C" else "°F"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Toggle button (°C/°F) and theme switcher (light/dark mode)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                SegmentedButton(viewModel)
                ThemeSwitcher(darkTheme, onThemeChange = onThemeToggle)
            }
        }
        item {
            // City text
            Text(
                text = city,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 12.dp)
            )
            // Date "Monday | 2025-05-05"
            Text(
                text = formattedDate,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            // CURRENT WEATHER
            weatherData.let { data ->
                // Icon
                val icon = getIcon(data.current_weather.weatherCode)
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "current weather icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(120.dp)
                        .padding(top = 18.dp, bottom = 14.dp)
                )
                // Temperature
                Text(
                    text = "${data.current_weather.temperature}${tempUnit}",
                    fontSize = 50.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                // Rain, wind and humidity
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // current_weather does not include precipitation_probability or humidity -> fetch from "hourly"
                        val currentTime =
                            data.current_weather.time.take(13) + ":00" // round down ("2025-05-05T15" + ":00")
                        val currentTimeIndex = data.hourly.time.indexOf(currentTime)

                        // Rain
                        CurrentWeatherDetails(
                            icon = R.drawable.umbrella1,
                            value = "${data.hourly.precipitation_probability.getOrNull(currentTimeIndex) ?: 0.0}%",
                            label = "Rain"
                        )
                        // Wind
                        CurrentWeatherDetails(
                            icon = R.drawable.wind1,
                            value = "${data.current_weather.windspeed} m/s",
                            label = "Wind Speed"
                        )
                        // Humidity
                        CurrentWeatherDetails(
                            icon = R.drawable.humidity,
                            value = "${
                                data.hourly.relative_humidity_2m.getOrNull(currentTimeIndex) ?: 0.0}%",
                            label = "Humidity"
                        )
                    }
                }
            }
        }
        // 7-DAY FORECAST
        item {
            Text("7-day forecast",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 18.dp),
                textAlign = TextAlign.Center
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                weatherData.let { data ->
                    items(data.daily.time.size) { index -> // index = daily.time[0, 1, 2 ..]
                        val date = formatForecastDate(data.daily.time[index]) // "2025-05-05" -> "05-05"
                        val icon = getIcon(data.daily.weather_code.getOrNull(index) ?: -1)
                        val temperature = data.daily.temperature_2m_max.getOrNull(index) ?: 0.0

                        Card(
                            modifier = Modifier
                                .clickable(onClick = {
                                    navController.navigate(
                                        "detailScreen/$index/${data.daily.time[index]}/$tempUnit") } // time= "2025-10-05"
                                ),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Date
                                Text(
                                    text = date,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                // Icon
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = "forecast icon",
                                    modifier = Modifier.size(50.dp).padding(bottom = 8.dp)
                                )
                                // Temperature
                                Text(
                                    text = "${temperature}${tempUnit}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
