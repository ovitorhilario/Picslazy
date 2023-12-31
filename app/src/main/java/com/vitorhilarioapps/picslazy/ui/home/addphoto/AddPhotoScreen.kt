package com.vitorhilarioapps.picslazy.ui.home.addphoto

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.shapes.itemOverlap
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mGray
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mLightGray
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.HexagonItem
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.HexagonType
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarMenu
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.common.singletons.Args

@SuppressLint("ResourceType")
@Composable
fun AddPhotoScreen(
    viewModel: AddPhotoViewModel = hiltViewModel<AddPhotoViewModel>(),
    onOpenProfile: () -> Unit,
    onEditPhoto: (String?) -> Unit
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val photos by viewModel.photosState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { viewModel.checkStorage() }
    )

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemSize = (if (maxHeight < maxWidth) maxHeight else maxWidth) * .5f
        val overlap = itemOverlap(LocalDensity.current, itemSize) + 4.dp

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopBarMenu(onClick = onOpenProfile, textCenter = stringResource(id = R.string.welcome_part_1) + userState.name)

            Spacer(modifier = Modifier.height(4.dp))

            Box(modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(15))
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.raw.banner_home),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(Color.Transparent, Color.Black))
                        )
                        .align(Alignment.Center)
                )

                Text(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(130.dp)
                        .align(Alignment.CenterEnd),
                    text = stringResource(id = R.string.welcome_part_2),
                    fontSize = Typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Medium,
                    lineHeight = Typography.titleSmall.lineHeight,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Vamos começar?",
                    fontSize = Typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_emoji_love))

                LottieAnimation(
                    modifier = Modifier
                        .size(48.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Divider(modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth())

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(overlap),
            ) {

                item {
                    Spacer(modifier = Modifier.height(itemSize - 76.dp))
                }

                item {
                    val uploadImageComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_upload))

                    HexagonItem(
                        type = if (uploadImageComposition != null) {
                            HexagonType.Animation(composition = uploadImageComposition!!)
                        } else {
                            HexagonType.Action(icon = painterResource(R.drawable.ic_add_image))
                        },
                        index = 0,
                        hexagonSize = itemSize,
                        onClick = { onEditPhoto(Args.PICK_IMAGE) }
                    )
                }

                when (photos) {
                    is AddPhotoViewModel.StorageState.Success -> {
                        itemsIndexed((photos as AddPhotoViewModel.StorageState.Success).data) { index, item ->
                            HexagonItem(
                                type = HexagonType.Photo(item.uri),
                                index = index + 1,
                                hexagonSize = itemSize,
                                onClick = { hexagonType ->
                                    (hexagonType as? HexagonType.Photo)?.uri?.let { photoUri ->
                                        onEditPhoto(Uri.encode(photoUri.toString()))
                                    }
                                }
                            )
                        }
                    }
                    is AddPhotoViewModel.StorageState.Failure -> {
                        // Show Error In UI
                        item {
                            HexagonItem(
                                type = HexagonType.Action(text = stringResource(R.string.error_in_load_images), icon = painterResource(R.drawable.ic_gallery)),
                                index = 1,
                                hexagonSize = itemSize,
                                onClick = { /* Do nothing yet */ }
                            )
                        }
                    }
                    is AddPhotoViewModel.StorageState.PermissionsDenied -> {
                        // Show that permissions was Denied
                        item {
                            HexagonItem(
                                type = HexagonType.Action(text = stringResource(R.string.grant_access_gallery), icon = painterResource(R.drawable.ic_gallery)),
                                index = 1,
                                hexagonSize = itemSize,
                                onClick = {
                                    val readPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

                                    launcher.launch(arrayOf(readPermission, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddPhotoScreen() {
    //AddPhotoScreen({}, {})
}
