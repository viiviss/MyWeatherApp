package com.example.myweatherapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A card component that displays weather details such as sunrise time, wind speed, etc.
 *
 * @param cardName Title of the card (e.g. "Sunrise").
 * @param value The value to display (e.g. "04:40").
 * @param icon Resource ID of the icon to visually represent the data.
 */
@Composable
fun DetailsCard(cardName: String, value: String, icon: Int) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .size(170.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = cardName,
                fontSize = 19.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = "detailsCard icon",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = value,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}