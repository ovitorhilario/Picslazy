package com.vitorhilarioapps.picslazy.common.designsystem.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mGray
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mRed
import com.vitorhilarioapps.picslazy.common.designsystem.widgets.InputTextPattern.inputTextModifier

@Composable
fun InputText(
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    notSpace: Boolean = true,
    error: String? = null,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier.inputTextModifier(),
            colors = InputTextPattern.getColors(),
            value = text,
            onValueChange = { onTextChange(if (notSpace) it.trim() else it) },
            placeholder = {
                Text(
                    text = placeHolder,
                    fontSize = Typography.bodyMedium.fontSize,
                    color = mGray
                )
            },
            singleLine = true,
            maxLines = 1,
            isError = error != null,
            shape = RoundedCornerShape(25)
        )

        error?.let { msg ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = msg,
                fontSize = Typography.bodySmall.fontSize,
                textAlign = TextAlign.Start,
                color = mRed
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PasswordInputText(
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    notSpace: Boolean = true,
    error: String? = null
) {

    var isVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier.inputTextModifier(),
            colors = InputTextPattern.getColors(),
            value = text,
            onValueChange = { onTextChange(if (notSpace) it.trim() else it) },
            placeholder = {
                Text(
                    text = placeHolder,
                    fontSize = Typography.bodyMedium.fontSize,
                    color = mGray
                )
            },
            trailingIcon = {

                val iconRes = if (isVisible)  {
                    painterResource(id = R.drawable.ic_eye_off)
                } else {
                    painterResource(id = R.drawable.ic_eye_on)
                }

                val interactionSource = remember { MutableInteractionSource() }

                AnimatedContent(isVisible, label = "") { toggle ->
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { isVisible = !isVisible },
                        painter = iconRes,
                        contentDescription = null,
                    )
                }
            },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            maxLines = 1,
            isError = error != null,
            shape = RoundedCornerShape(25)
        )

        error?.let { msg ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = msg,
                fontSize = Typography.bodySmall.fontSize,
                textAlign = TextAlign.Start,
                color = mRed
            )
        }
    }
}

object InputTextPattern {

    fun Modifier.inputTextModifier() = this
        .fillMaxWidth()
        .height(56.dp)

    @Composable
    fun getColors() = OutlinedTextFieldDefaults.colors(
        // Text Color
        unfocusedTextColor = Color.Black,
        disabledTextColor = Color.Black,
        focusedTextColor = Color.Black,
        errorTextColor = mRed,

        // Supporting Text Color
        unfocusedSupportingTextColor = Color.Black,
        disabledSupportingTextColor = Color.Black,
        errorSupportingTextColor = mRed,
        focusedSupportingTextColor = Color.Black,

        /*
        // Container Color
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        errorContainerColor = Color.White,
        focusedContainerColor = Color.White,
        */

        // Selection Color
        selectionColors = TextSelectionColors(Color.Black, Color.LightGray),

        // Border Color
        disabledBorderColor = Color.Black,
        errorBorderColor = mRed,
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
    )
}