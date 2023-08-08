package com.vitorhilarioapps.picslazy.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
    onFinishSplash: (Boolean) -> Unit
) {
    var lifecycleIsStarted by rememberSaveable { mutableStateOf(false) }

    LifecycleStartEffect(LocalLifecycleOwner.current) {
        lifecycleIsStarted = true

        onStopOrDispose {
            // TODO
        }
    }

    LaunchedEffect(lifecycleIsStarted) {
        if (lifecycleIsStarted) {
            delay(2000L)
            onFinishSplash(viewModel.isAuthenticated())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.app_name_uppercase),
            fontSize = Typography.displayMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}