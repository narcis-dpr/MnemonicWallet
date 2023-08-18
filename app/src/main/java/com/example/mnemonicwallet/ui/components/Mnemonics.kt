package com.example.mnemonicwallet.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Mnemonics(mnemonics: List<String>) {
    Card(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp,
            ),
        ) {
            items(mnemonics.size) { i ->
                Text(
                    text = mnemonics[i],
                    fontSize = 25.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.wrapContentSize(),
                    overflow = TextOverflow.Visible,
                    
                )
            }
        }
    }
}

@Composable
@Preview
private fun preview() {
    val mnemonic = listOf(
        "drama", "print", "suspect", "patrol", "seek", "estate",
        "thrive", "since", "remind", "march", "letter", "era",
    )
    Mnemonics(mnemonics = mnemonic)
}
