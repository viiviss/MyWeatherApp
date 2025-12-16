package com.example.myweatherapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.myweatherapp.components.ThemeSwitcher
import com.example.myweatherapp.data.NetworkResponse
import com.example.myweatherapp.view.WeatherViewModel
/**
 * The start screen of the weather application.
 *
 * This composable function displays the UI where user can:
 * - Toggle between light and dark theme
 * - Enter a city or country name
 * - Search for weather data
 * - View loading state, error messages, or search results
 *
 * @param navController Used for navigating to other screens.
 * @param viewModel Shared ViewModel that provides weather data and state.
 * @param darkTheme A boolean indicating whether dark mode is enabled.
 * @param onThemeToggle A callback function to toggle theme between light and dark.
 */
@Composable
fun StartScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    // collectAsState = collects values from a Flow and transforms it into Compose state
    val uiState by viewModel.weatherData.collectAsState()
    var city by rememberSaveable { mutableStateOf("") }
    val searchedCity by viewModel.cityName.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        // dark mode switch
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                Text(
                    text = "Dark mode",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(5.dp))
                ThemeSwitcher(darkTheme, onThemeChange = onThemeToggle)
            }
        }
        // headline
        item {
            Text(
                text = "Weather App",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 150.dp, bottom = 46.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        // text field
        item {
            Text(
                text ="Enter a city or country name:",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp, bottom = 8.dp)
            )
            TextField(
                value = city,
                onValueChange = { city = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp),
                placeholder = {
                    Text("City/Country name",
                    fontSize = 18.sp,
                    color = Color.Gray) },
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                )
            )
        }
        // buttons
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Search button
                    Button(
                        onClick = { viewModel.setLocation(city) }
                    ) {
                        Text(
                            text = "Search",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    // Clear button
                    Button(
                        onClick = {
                            viewModel.clear()
                            city = ""
                        }
                    ) {
                        Text(
                            text = "Clear",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                // Error message
                if (!errorMessage.isNullOrEmpty()) { // check that it is not empty or null
                    Text(
                        text = errorMessage!!, // assert non-null
                        color = Color.Red,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }
        item {
            // when-expression used with a sealed class -> the compiler ensures you cover all cases (avoid ignoring a possible state)
            when(val result = uiState) {
                is NetworkResponse.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is NetworkResponse.Error -> {
                    Text(
                        text = result.message,
                        fontSize = 18.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                is NetworkResponse.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Results:",
                            fontSize = 23.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // city text
                                Text(
                                    text = searchedCity,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                // show weather button
                                Button(
                                    modifier = Modifier.padding(top = 18.dp),
                                    colors = ButtonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.Black,
                                        disabledContainerColor = Color.Black,
                                        disabledContentColor = Color.Black
                                    ),
                                    onClick = { navController.navigate("weatherScreen") }
                                ) {
                                    Text(
                                        text = "Show weather",
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(3.dp),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                null -> {} // do nothing
            }
        }
    }
}
