package com.example.mnemonicwallet.ui.components

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedTextView(text: String, modifier: Modifier = Modifier) {
    Card(elevation = 8.dp,
    modifier = modifier,
    backgroundColor = Color.LightGray) {

    }
}
