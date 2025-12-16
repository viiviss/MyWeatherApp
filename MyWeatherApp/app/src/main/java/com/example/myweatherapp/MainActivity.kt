package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myweatherapp.data.NetworkResponse
import com.example.myweatherapp.screens.DetailScreen
import com.example.myweatherapp.screens.WeatherScreen
import com.example.myweatherapp.screens.StartScreen
import com.example.myweatherapp.view.WeatherViewModel

/**
 * Main entry point of the application.
 *
 * This activity sets up the app's theme, handles state for dark mode,
 * and launches the main content using Jetpack Compose.
 *
 * @author Viivi Siren
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }

            MyWeatherAppTheme(darkTheme = darkTheme, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(
                        darkTheme = darkTheme,
                        onThemeToggle = { darkTheme = !darkTheme } // lambda that changes the state
                    )
                }
            }
        }
    }
}

/**
 * Sets up the navigation structure of the weather app.
 *
 * @param darkTheme A boolean indicating whether dark theme is enabled.
 * @param onThemeToggle A callback function that toggles the theme mode.
 */
@Composable
fun App(darkTheme: Boolean, onThemeToggle: () -> Unit) {
    // create a NavController to handle navigation between composables
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel()

    // define a navigation host that controls the navigation
    NavHost(navController = navController, startDestination = "startScreen") {
        composable("startScreen") {
            StartScreen(navController, viewModel, darkTheme, onThemeToggle)
        }

        composable("weatherScreen") {
            val uiState by viewModel.weatherData.collectAsState()

            if (uiState is NetworkResponse.Success) {
                val data = (uiState as NetworkResponse.Success).data
                WeatherScreen(navController, data, viewModel, darkTheme, onThemeToggle)
            }
        }

        composable(
            route = "detailScreen/{index}/{date}/{tempUnit}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("date") { type = NavType.StringType },
                navArgument("tempUnit") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Retrieve the "index", "date" and "tempUnit" arguments from the backStackEntry's arguments
            // these values are passed when navigating to "detailScreen"
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val tempUnit = backStackEntry.arguments?.getString("tempUnit") ?: ""

            DetailScreen(navController, index, date, tempUnit, viewModel)
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyWeatherAppTheme {
        Greeting("Android")
    }
}*/