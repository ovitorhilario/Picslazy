package com.vitorhilarioapps.picslazy.ui.login.forgotpassword

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.FiledButton
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.InputText
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarNavigateBack
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.common.model.InvalidEntry
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel<ForgotPasswordViewModel>(),
    onBackNavigate: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(null)
    val invalidEntry = InvalidEntry(context)

    var animIsPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(events) {
        when(events) {
            is Events.Info -> {
                Toast.makeText(context, context.getString(R.string.info) + (events as Events.Info).info, Toast.LENGTH_SHORT).show()
            }
            is Events.Success -> {
                animIsPlaying = true
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
                .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(300.dp),
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_email))
                val targetValue by animateFloatAsState(targetValue = if (animIsPlaying) 1f else 0f, animationSpec = tween(3000))

                LottieAnimation(
                    composition = composition,
                    progress = {
                        if (targetValue == 1f) {
                            animIsPlaying = false
                            0f
                        } else {
                            if (animIsPlaying) targetValue else 0f
                        }
                    }
                )
            }

            Text(text = stringResource(R.string.reset_your_password))
            
            InputText(
                text = uiState.email,
                onTextChange = viewModel::onEmailChange,
                placeHolder = stringResource(id = R.string.email),
                error = if (errorState.invalidEmail) invalidEntry.EMAIL else null,
            )

            FiledButton(
                text = stringResource(R.string.send_reset_link),
                onClick = viewModel::onSendRecoveryEmail,
                isLoading = taskState == TaskState.Loading
            )
        }
    }
}