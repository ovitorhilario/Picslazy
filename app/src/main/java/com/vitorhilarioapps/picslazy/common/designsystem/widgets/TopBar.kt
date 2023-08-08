package com.vitorhilarioapps.picslazy.common.designsystem.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography

@Composable
fun TopBarNavigateBack(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        IconExtended(
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            onClick = onClick
        )
    }
}

@Composable
fun TopBarMenu(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconExtended(
            imageVector = Icons.Rounded.Menu,
            onClick = onClick
        )
    }
}

@Composable
fun TopBarActions(
    actionName: String,
    onBackNavigate: () -> Unit,
    onAction: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconExtended(
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            onClick = onBackNavigate
        )

        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(50))
                .clickable(enabled = !isLoading, onClick = onAction)
        ) {
            if (isLoading) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_saving))

                LottieAnimation(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(48.dp)
                        .align(Alignment.Center),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = actionName,
                    fontSize = Typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewTopBarNavigateBack() {
    TopBarNavigateBack({})
}

@Preview
@Composable
fun PreviewTopBarActions() {
    TopBarActions("Save", {}, {}, false)
}

@Preview
@Composable
fun PreviewTopBarMenu() {
    TopBarMenu {}
}