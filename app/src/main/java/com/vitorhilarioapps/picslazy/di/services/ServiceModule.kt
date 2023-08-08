package com.vitorhilarioapps.picslazy.di.services

import com.google.firebase.auth.FirebaseAuth
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import com.vitorhilarioapps.picslazy.data.remote.services.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun providesAccountService(
        accountServiceImpl: AccountServiceImpl
    ): AccountService
}