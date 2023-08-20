package com.example.mnemonicwallet.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemonicwallet.ui.viewModels.SignViewModel
import com.example.mnemonicwallet.utiles.data

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignScreen(
    route: String,
    context: Context,
    viewModel: SignViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = Color.White),
        ) {
        val amount = remember { mutableStateOf(TextFieldValue()) }
        val input1 = remember { mutableStateOf(TextFieldValue()) }
        val input2 = remember { mutableStateOf(TextFieldValue()) }
        val output1 = remember { mutableStateOf(TextFieldValue()) }
        val output2 = remember { mutableStateOf(TextFieldValue()) }
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = "Message:", fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp),
            backgroundColor = Color.LightGray,
        ) {
            Column {
                TextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    ),
                    modifier = Modifier.padding(8.dp).defaultMinSize(minHeight = 16.dp),
                    label = { Text(text = "amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                TextField(
                    value = input1.value,
                    onValueChange = { input1.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    ),
                    modifier = Modifier.padding(8.dp).defaultMinSize(minHeight = 16.dp),
                    label = { Text(text = "input1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                TextField(
                    value = input2.value,
                    onValueChange = { input2.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    ),
                    modifier = Modifier.padding(8.dp).defaultMinSize(minHeight = 16.dp),
                    label = { Text(text = "input2") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                TextField(
                    value = output1.value,
                    onValueChange = { output1.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    ),
                    modifier = Modifier.padding(8.dp).defaultMinSize(minHeight = 16.dp),
                    label = { Text(text = "output1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                TextField(
                    value = output2.value,
                    onValueChange = { output2.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray
                    ),
                    modifier = Modifier.padding(8.dp).defaultMinSize(minHeight = 16.dp),
                    label = { Text(text = "output2") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Nonce:", fontWeight = FontWeight.Bold,
                color = Color.Black, fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            ElevatedTextView(text = "37")
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Sign:", fontWeight = FontWeight.Bold,
                color = Color.Black, fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            viewModel.sign.observeAsState().value?.data?.let { ElevatedTextView(text = it) }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.signMessage(
                        context,
                        amount.value.text,
                        input1.value.text,
                        input2.value.text.toBigInteger(),
                        output1.value.text.toBigInteger(),
                        output2.value.text.toBigInteger(),
                        37
                    )
                },
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(text = "Sign")
            }
        }
}

@Composable
@Preview
private fun Preview() {
    // SignScreen(Navigation.SignScreen.route, context = LocalContext)
}
