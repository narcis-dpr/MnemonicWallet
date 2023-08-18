package com.example.mnemonicwallet.data.model

data class W3JLWallet(
    val source: Source = Source.NONE,
    val address: String = "",
    val mnemonic: String = "",
    val privateKey: String = "",
    val publicKey: String = "",
    val jsonSource: String = "",
    val createAt: Long = 0,
)

enum class Source {
    PRIVATE_KEY,
    MNEMONIC,
    JSON,
    NONE,
}
