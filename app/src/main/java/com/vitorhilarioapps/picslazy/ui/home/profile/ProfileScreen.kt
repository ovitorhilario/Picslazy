package com.vitorhilarioapps.picslazy.ui.home.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mRed
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mRedLight
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.IconExtended
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarMenu
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarNavigateBack
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    onBackNavigate: () -> Unit,
    onSingOut: () -> Unit
) {

    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopBarNavigateBack(onClick = onBackNavigate)

            Box(
                modifier = Modifier
                    .size(160.dp),
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_profile))

                LottieAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.fillMaxWidth())

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.LightGray
            )

            UserInfo(name = userState.name, email = userState.email)
        }


        Box(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clip(RoundedCornerShape(50))
                .background(mRedLight)
                .clickable {
                    scope.launch {
                        viewModel.logOut()
                        onSingOut()
                    }
                }
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(vertical = 10.dp, horizontal = 32.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.log_out),
                fontSize = Typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium,
                color = mRed
            )
        }


    }
}

@Composable
fun UserInfo(
    name: String,
    email: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TableRow(
            label = stringResource(R.string.name),
            text = name
        )

        TableRow(
            label = stringResource(R.string.email),
            text = email
        )
    }
}

@Composable
fun TableRow(
    label: String,
    text: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            fontSize = Typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Medium
        )

        Text(
            modifier = Modifier.weight(2f),
            text = text,
            fontSize = Typography.bodyMedium.fontSize,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(onBackNavigate = {}, onSingOut = {})
}