package com.vitorhilarioapps.picslazy.ui.home.editphoto

import android.content.ContentProvider
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.TopBarActions
import com.vitorhilarioapps.picslazy.common.singletons.Args
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditPhotoScreen(
    viewModel: EditPhotoViewModel = hiltViewModel<EditPhotoViewModel>(),
    args: String?,
    onBackNavigate: () -> Unit,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoState by viewModel.photoState.collectAsStateWithLifecycle()
    val filterState by viewModel.filtersState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.setOriginUri(it) } ?: onBackNavigate()
        }
    )

    LaunchedEffect(Unit) {
        args?.let { content ->
            if (content == Args.PICK_IMAGE) {
                val request = PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()

                imagePicker.launch(request)
            } else {
                Uri.parse(Uri.decode(content)).let {
                    viewModel.setOriginUri(it)
                }
            }
        } ?: onBackNavigate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        val successSave = stringResource(R.string.success_save)
        val errorSave = stringResource(R.string.error_save)

        TopBarActions(
            actionName = stringResource(R.string.save),
            onBackNavigate = onBackNavigate,
            onAction = {
                if (saveState == EditPhotoViewModel.SaveState.Loading) return@TopBarActions

                scope.launch {
                    val success = viewModel.saveSelectedBitmap()
                    val msg = if (success) successSave else errorSave

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            },
            isLoading = saveState == EditPhotoViewModel.SaveState.Loading
        )

        AnimatedContent(
            targetState = photoState,
            label = "",
            transitionSpec = {
                fadeIn() with fadeOut()
            },
            content = {
                photoState.selectedBitmap?.let { bitmap ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(460.dp)
                            .background(Color.LightGray)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(460.dp),
                            model = ImageRequest.Builder(context)
                                .data(bitmap)
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loading))

                        LottieAnimation(
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.Center),
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.wrapContentHeight())
            }

            itemsIndexed(
                items = filterState.data,
                key = { index, _ -> index },
                itemContent = { index, (bitmap, title) ->

                    Column(
                        modifier = Modifier
                            .width(64.dp)
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
                    ) {
                        
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            fontSize = Typography.labelSmall.fontSize,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                        
                        AsyncImage(
                            modifier = Modifier
                                .size(64.dp)
                                .clickable { viewModel.updatePhotoState(index) }
                                .clip(RoundedCornerShape(8.dp)),
                            model = ImageRequest.Builder(context)
                                .data(bitmap)
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }
            )

            item {
                Spacer(modifier = Modifier.wrapContentHeight())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditPhotoScreen() {
    //
}