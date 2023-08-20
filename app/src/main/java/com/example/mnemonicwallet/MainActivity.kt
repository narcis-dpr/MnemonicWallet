package com.example.mnemonicwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mnemonicwallet.data.repository.W3JLFactory
import com.example.mnemonicwallet.ui.components.MnemonicScreen
import com.example.mnemonicwallet.ui.components.SignScreen
import com.example.mnemonicwallet.ui.navigation.Navigation
import com.example.mnemonicwallet.ui.theme.MnemonicWalletTheme
import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val compositeDisposable = CompositeDisposable()
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MnemonicWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Navigation.MnemonicScreen.route,
                    ) {
                        composable(
                            route = Navigation.MnemonicScreen.route,
                        ) {
                            MnemonicScreen(navController = navController, this@MainActivity)
                        }
                        composable(route = Navigation.SignScreen.route + "/{privateKey}" + "/{address}") {
                            SignScreen(Navigation.SignScreen.route, this@MainActivity)
                        }
                    }
                }
            }
        }
    }

    private fun displayWallet(result: String) {
        //        txtHome.text = result
//        Greeting(name = result)
    }

    public override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MnemonicWalletTheme {
        Greeting("Android")
    }
}
