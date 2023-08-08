package com.vitorhilarioapps.picslazy.ui.home.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.data.remote.model.User
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val _userState = MutableStateFlow<User>(User())
    val userState: StateFlow<User> = _userState

    init {
        _userState.value = getUserInfo()
    }

    private fun getUserInfo(): User {
        val firebaseUser = accountService.currentUser

        return User(
            firebaseUser?.uid ?: context.getString(R.string.invalid_id),
            firebaseUser?.displayName ?: context.getString(R.string.invalid_user_name),
            firebaseUser?.email ?: context.getString(R.string.invalid_email),
            firebaseUser?.photoUrl,
            firebaseUser?.isEmailVerified ?: false,
            firebaseUser?.isAnonymous ?: false
        )
    }

    suspend fun logOut() {
        val logOutTask = viewModelScope.launch(Dispatchers.IO) {
            accountService.signOut()
        }

        logOutTask.join()
    }
}