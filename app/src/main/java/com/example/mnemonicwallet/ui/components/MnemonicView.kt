package com.example.mnemonicwallet.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mnemonicwallet.ui.navigation.Navigation
import com.example.mnemonicwallet.ui.viewModels.MnemonicsViewModel
import com.example.mnemonicwallet.utiles.data

@Composable
fun MnemonicScreen(
    navController: NavHostController,
    context: Context,
    viewModel: MnemonicsViewModel = hiltViewModel(),
) {
    // val scroll = rememberScrollableState(consumeScrollDelta = a)
    val mnemonic = listOf(
        "drama", "print", "suspect", "patrol", "seek", "estate",
        "thrive", "since", "remind", "march", "letter", "era",
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = "Mnemonic:", fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        viewModel.mnemonics.observeAsState().value?.let { Mnemonics(mnemonics = it) }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Address:", fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        viewModel.wallet.observeAsState().value?.data?.let { ElevatedTextView(text = it.address) }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Private:", fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        viewModel.wallet.observeAsState().value?.data?.privateKey?.let { ElevatedTextView(text = it) }
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(
                onClick = { viewModel.createWallet(context) },
                modifier = Modifier.weight(0.75f).padding(start = 4.dp)
            ) {
                Text(text = "Re-Generate")
            }
            Button(
                onClick = { navController.navigate(Navigation.SignScreen.route +
                        "/${viewModel.wallet.value?.data?.privateKey}" + "/${viewModel.wallet.value?.data?.address}") },
                modifier = Modifier.wrapContentSize().weight(1f)
            ) {
                Text(text = "Sign a message", maxLines = 1, overflow = TextOverflow.Visible)
            }
        }
    }
}

@Preview
@Composable
private fun preview() {
    val navController = rememberNavController()
    //   MnemonicScreen(navController, this)
}
