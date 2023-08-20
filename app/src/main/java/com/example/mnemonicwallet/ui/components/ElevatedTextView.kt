package com.example.mnemonicwallet.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedTextView(text: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        elevation = 8.dp,
        modifier = modifier.padding(16.dp),
        backgroundColor = Color.LightGray,
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = modifier.padding(
                top = 16.dp, bottom = 32.dp,
                start = 16.dp, end = 16.dp,
            ).clickable(true, onClick = {
                copyToClipboard(context, text)
            }),
        )
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("sign", text)
    clipboardManager.setPrimaryClip(clip)
    Toast.makeText(context, "copy to clipboard", Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
private fun preview() {
    ElevatedTextView(text = "0xefsfsdfjsndfklsdjfsdjfdfkldfjsdfsdf")
}
