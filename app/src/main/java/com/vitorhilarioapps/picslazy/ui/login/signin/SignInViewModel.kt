package com.vitorhilarioapps.picslazy.ui.login.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.data.remote.model.TaskResult
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import com.vitorhilarioapps.picslazy.common.ext.isValidEmail
import com.vitorhilarioapps.picslazy.common.ext.isValidPassword
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.ui.login.forgotpassword.ForgotPasswordError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState: StateFlow<SignInState> = _uiState

    private val _errorState = MutableStateFlow(SignInError())
    val errorState: StateFlow<SignInError> = _errorState

    private val _taskState: MutableStateFlow<TaskState> = MutableStateFlow(TaskState.Idle)
    val taskState: StateFlow<TaskState> = _taskState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _eventsChannel = Channel<Events>()

    private val _events = flow {
        while (true) {
            _eventsChannel.tryReceive().getOrNull()?.let { emit(it) }
            delay(4000L)
        }
    }

    val events
        get() = _events

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        _uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick() {
        clearErrors()

        if (!email.isValidEmail()) {
            _errorState.value = _errorState.value.copy(invalidEmail = true)
            return
        }

        if (!password.isValidPassword()) {
            _errorState.value = _errorState.value.copy(invalidPassword = true)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            _taskState.value = TaskState.Loading

            try {
                val loginTaskResult = accountService.authenticate(email, password)

                when(loginTaskResult) {
                    is TaskResult.Success -> updateAuth()
                    is TaskResult.Failure -> loginTaskResult.throwable?.message?.let { error -> _eventsChannel.send(Events.Error(error)) }
                }
            } catch (e: Exception) {
                _eventsChannel.send(Events.Error(e.message ?: context.getString(R.string.auth_error)))
            }

            _taskState.value = TaskState.Idle
        }
    }

    private suspend fun updateAuth() {
        val userIsValid = accountService.currentUser != null
        val emailIsVerified = accountService.currentUser?.isEmailVerified ?: false

        if (userIsValid && !emailIsVerified) {
            _eventsChannel.send(Events.Info(context.getString(R.string.verification_email_required)))

        }

        _isAuthenticated.value = userIsValid && emailIsVerified
    }

    private fun clearErrors() {
        _errorState.value = _errorState.value.copy(invalidEmail = false, invalidPassword = false)
    }
}