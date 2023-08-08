package com.vitorhilarioapps.picslazy.ui.splash

import androidx.lifecycle.ViewModel
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    fun isAuthenticated(): Boolean {
        return accountService.currentUser != null && accountService.currentUser?.isEmailVerified ?: false
    }
}