package com.example.mnemonicwallet.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElevatedEditText( modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val inputMessage = remember { mutableStateOf(TextFieldValue()) }
    Card(
        elevation = 8.dp,
        modifier = modifier.padding(16.dp),
        backgroundColor = Color.LightGray,
    ) {
        TextField(
            value = inputMessage.value,
            onValueChange = { inputMessage.value = it },
            modifier = Modifier.padding(16.dp).defaultMinSize(minHeight = 50.dp)
        )
    }
}
