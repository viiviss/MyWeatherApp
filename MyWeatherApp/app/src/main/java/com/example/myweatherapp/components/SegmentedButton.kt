package com.example.myweatherapp.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import com.example.myweatherapp.view.WeatherViewModel

/**
 * A segmented button that allows switching between Celsius and Fahrenheit temperature units.
 *
 * @param viewModel The ViewModel holding the current temperature unit state.
 */
@Composable
fun SegmentedButton(viewModel: WeatherViewModel) {
    val selectedIndex by viewModel.unit.collectAsState()
    val options = listOf("°C", "°F")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { viewModel.setUnit(index) },
                selected = index == selectedIndex,
                label = { Text(label, fontSize = 16.sp) }
            )
        }
    }
}