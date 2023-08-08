package com.vitorhilarioapps.picslazy.ui.login.signup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.FiledButton
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.InputText
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.PasswordInputText
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarNavigateBack
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.common.model.InvalidEntry
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>(),
    onBackNavigate: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TopBarNavigateBack(onClick = onBackNavigate)

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(160.dp),
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_sing_up))

                LottieAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = stringResource(R.string.create_your_account),
                fontSize = Typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium
            )

            InputText(
                text = uiState.name,
                onTextChange = viewModel::onNameChange,
                placeHolder = stringResource(id = R.string.name),
                notSpace = false,
                error = if (errorState.invalidName) invalidEntry.NAME else null
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
                placeHolder = stringResource(id = R.string.password),
                error = if (errorState.invalidPassword) invalidEntry.PASSWORD else null
            )

            PasswordInputText(
                text = uiState.confirmPassword,
                onTextChange = viewModel::onConfirmPasswordChange,
                placeHolder = stringResource(R.string.confirm_password),
                error = if (errorState.invalidConfirmPassword) invalidEntry.PASSWORD else null
            )

            FiledButton(
                text = stringResource(R.string.sign_up),
                onClick = viewModel::onSignUpClick,
                isLoading = taskState == TaskState.Loading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    //SignUpScreen()
}