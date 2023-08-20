package com.example.mnemonicwallet.di

import com.example.mnemonicwallet.data.repository.BaseConfigRepository
import com.example.mnemonicwallet.data.repository.BaseConfigService
import com.example.mnemonicwallet.data.repository.EthRepository
import com.example.mnemonicwallet.data.repository.EthService
import com.example.mnemonicwallet.data.repository.TokenRepository
import com.example.mnemonicwallet.data.repository.TokenService
import com.example.mnemonicwallet.data.repository.WalletRepository
import com.example.mnemonicwallet.data.repository.WalletService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindWalletRepository(walletService: WalletService): WalletRepository

    @Binds
    abstract fun bindTokenRepository(tokenService: TokenService): TokenRepository

    @Binds
    abstract fun bindEthRepository(ethService: EthService): EthRepository

    @Binds
    abstract fun bindBaseConfigRepository(baseConfigService: BaseConfigService): BaseConfigRepository
}
