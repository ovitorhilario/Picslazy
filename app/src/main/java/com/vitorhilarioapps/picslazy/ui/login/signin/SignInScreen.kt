package com.vitorhilarioapps.picslazy.ui.login.signin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.FiledButton
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.InputText
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.OutlinedButton
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.PasswordInputText
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.common.model.InvalidEntry
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel<SignInViewModel>(),
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
    val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(null)
    val invalidEntry = InvalidEntry(context)

    LaunchedEffect(events) {
        when(events) {
            is Events.Info -> {
                Toast.makeText(context, context.getString(R.string.info) + (events as Events.Info).info, Toast.LENGTH_SHORT).show()
            }
            is Events.Success -> {
                Toast.makeText(context, context.getString(R.string.success) + (events as Events.Success).message, Toast.LENGTH_SHORT).show()
            }
            is Events.Error -> {
                Toast.makeText(context, context.getString(R.string.error) + (events as Events.Error).error, Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }
    }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .padding(top = 100.dp, bottom = 48.dp),
                text = stringResource(id = R.string.app_name_uppercase),
                fontSize = Typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            InputText(
                text = uiState.email,
                onTextChange = viewModel::onEmailChange,
                placeHolder = stringResource(id = R.string.email),
                error = if (errorState.invalidEmail) invalidEntry.EMAIL else null
            )

            PasswordInputText(
                text = uiState.password,
                onTextChange = viewModel::onPasswordChange,
                placeHolder = stringResource(R.string.password),
                error = if (errorState.invalidPassword) invalidEntry.PASSWORD else null
            )

            FiledButton(
                text = stringResource(R.string.log_in),
                onClick = viewModel::onSignInClick,
                isLoading = taskState == TaskState.Loading
            )

            Text(
                modifier = Modifier.clickable(onClick = navigateToForgotPassword),
                text = stringResource(R.string.forgot_password),
                fontSize = Typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedButton(
            text = stringResource(R.string.create_account),
            onClick = navigateToSignUp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}
