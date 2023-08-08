package com.vitorhilarioapps.picslazy.common.designsystem.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vitorhilarioapps.picslazy.common.designsystem.theme.Typography
import com.vitorhilarioapps.picslazy.common.designsystem.theme.mGray

@Composable
fun FiledButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.Black,
            disabledContainerColor = mGray,
            disabledContentColor = mGray
        ),
        shape = RoundedCornerShape(25)
    ) {
        if (isLoading) {
           CircularProgressIndicator(
               modifier = Modifier.size(24.dp),
               color = Color.White
           )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = Typography.bodyMedium.fontSize,
            )
        }
    }
}

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(25)),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        )
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = Typography.bodyMedium.fontSize,
        )
    }
}
