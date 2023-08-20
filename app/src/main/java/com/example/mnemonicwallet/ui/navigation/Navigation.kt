package com.example.mnemonicwallet.ui.navigation

sealed class Navigation(val route: String) {
    object MnemonicScreen : Navigation("mnemonics_screen")
    object SignScreen : Navigation("sign_screen")
}
