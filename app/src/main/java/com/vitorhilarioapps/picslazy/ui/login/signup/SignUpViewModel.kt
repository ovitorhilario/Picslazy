package com.vitorhilarioapps.picslazy.ui.login.signup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.data.remote.model.TaskResult
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import com.vitorhilarioapps.picslazy.common.ext.isValidEmail
import com.vitorhilarioapps.picslazy.common.ext.isValidPassword
import com.vitorhilarioapps.picslazy.common.ext.passwordMatches
import com.vitorhilarioapps.picslazy.common.model.Events
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
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState

    private val _errorState = MutableStateFlow(SignUpError())
    val errorState: StateFlow<SignUpError> = _errorState

    private val _taskState: MutableStateFlow<TaskState> = MutableStateFlow(TaskState.Idle)
    val taskState: StateFlow<TaskState> = _taskState

    private val _eventsChannel = Channel<Events>()

    private val _events = flow {
        while (true) {
            _eventsChannel.tryReceive().getOrNull()?.let { emit(it) }
            delay(4000L)
        }
    }

    val events
        get() = _events

    private val name
        get() = uiState.value.name

    private val avatar
        get() = uiState.value.avatar

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    private val confirmPassword
        get() = uiState.value.confirmPassword

    fun onNameChange(newValue: String) {
        _uiState.value = uiState.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = uiState.value.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = uiState.value.copy(confirmPassword = newValue)
    }

    fun onAvatarChange(uri: Uri) {
        _uiState.value = uiState.value.copy(avatar = uri)
    }

    fun onSignUpClick() {

        clearErrors()

        if (name.isBlank()) {
            _errorState.value = _errorState.value.copy(invalidName = true)
            return
        }

        if (!email.isValidEmail()) {
            _errorState.value = _errorState.value.copy(invalidEmail = true)
            return
        }

        if (!password.isValidPassword()) {
            _errorState.value = _errorState.value.copy(invalidPassword = true)
            return
        }

        if (!confirmPassword.isValidPassword()) {
            _errorState.value = _errorState.value.copy(invalidConfirmPassword = true)
            return
        }

        if (!password.passwordMatches(confirmPassword)) {
            _errorState.value = _errorState.value.copy(invalidMatch = true)
            return
        }


        viewModelScope.launch(Dispatchers.IO) {

            _taskState.value = TaskState.Loading

            try {
                val createAccountTask = accountService.createAccount(name, email, password, avatar)

                when(createAccountTask) {
                    is TaskResult.Success -> {
                        val sendVerificationEmailTask = accountService.sendVerificationEmail()

                        if (sendVerificationEmailTask is TaskResult.Success) {
                            _eventsChannel.send(Events.Success(context.getString(R.string.verification_email_sent)))
                        } else {
                            _eventsChannel.send(Events.Error(context.getString(R.string.verification_email_sent_error)))
                        }
                    }
                    is TaskResult.Failure -> createAccountTask.throwable?.message?.let { error -> _eventsChannel.send(Events.Error(error)) }
                }
            } catch (e: Exception) {
                e.cause?.message?.let { error -> _eventsChannel.send(Events.Error(error)) }
            }

            _taskState.value = TaskState.Idle
        }
    }

    private fun clearErrors() {
        _errorState.value = _errorState.value.copy(
            invalidName = false,
            invalidEmail = false,
            invalidPassword = false,
            invalidConfirmPassword = false,
            invalidMatch = false
        )
    }
}