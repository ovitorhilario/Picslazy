package com.vitorhilarioapps.picslazy.common.designsystem.widgets

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.shapes.HexagonShape
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mLightGray

// Medium article: https://nglauber.medium.com/lazy-layout-hexagonal-no-jetpack-compose-af0e98fab136

sealed class HexagonType {
    data class Photo(val uri: Uri?): HexagonType()
    data class Action(val text: String? = null, val icon: Painter): HexagonType()
    data class Animation(val composition: LottieComposition): HexagonType()
}

@Composable
fun HexagonItem(
    type: HexagonType,
    index: Int,
    hexagonSize: Dp,
    onClick: (HexagonType) -> Unit,
) {
    val paddingValue = 8.dp + hexagonSize * .75f

    Box(
        modifier = Modifier
            .padding(
                start = if (index % 2 == 1) paddingValue else 0.dp,
                end = if (index % 2 == 0) paddingValue else 0.dp,
            )
            .size(hexagonSize)
            .clip(HexagonShape)
            .background(mLightGray)
            .clickable { onClick(type) },
        contentAlignment = Alignment.Center
    ) {

        when(type) {
            is HexagonType.Photo -> {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(type.uri)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            is HexagonType.Action -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(64.dp),
                        painter = type.icon,
                        contentDescription = null,
                        tint = Color.DarkGray
                    )

                    type.text?.let {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth(),
                            text = type.text,
                            fontSize = Typography.labelSmall.fontSize,
                            fontWeight = FontWeight.Medium,
                            lineHeight = Typography.labelSmall.fontSize,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
            }
            is HexagonType.Animation -> {
                LottieAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    composition = type.composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

