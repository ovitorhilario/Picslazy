package com.vitorhilarioapps.picslazy.ui.login.forgotpassword

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.ext.isValidEmail
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.data.remote.model.TaskResult
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
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
class ForgotPasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    private val _errorState = MutableStateFlow(ForgotPasswordError())
    val errorState: StateFlow<ForgotPasswordError> = _errorState

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

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onSendRecoveryEmail() {

        clearErrors()

        if (!_uiState.value.email.isValidEmail()) {
            _errorState.value = _errorState.value.copy(invalidEmail = true)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            _taskState.value = TaskState.Loading

            try {
                when(val sendRecoveryTask = accountService.sendRecoveryEmail(uiState.value.email)) {
                    is TaskResult.Success -> _eventsChannel.send(Events.Success(context.getString(R.string.email_send)))
                    is TaskResult.Failure -> sendRecoveryTask.throwable?.message?.let { error -> _eventsChannel.send(Events.Error(error)) }
                }
            } catch (e: Exception) {
                _eventsChannel.send(Events.Error(context.getString(R.string.error_sending_email)))
            }

            _taskState.value = TaskState.Idle
        }
    }

    fun clearErrors() {
        _errorState.value = _errorState.value.copy(invalidEmail = false)
    }
}