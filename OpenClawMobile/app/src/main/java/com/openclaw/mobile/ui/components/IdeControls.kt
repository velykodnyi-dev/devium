package com.openclaw.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.theme.*

@Composable
fun IdeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isSecondary: Boolean = false,
    isDestructive: Boolean = false
) {
    val backgroundColor = when {
        !enabled -> IdeSurfaceLight
        isDestructive -> IdeError
        isSecondary -> IdeSurface
        else -> IdeAccent
    }

    val textColor = when {
        !enabled -> IdeTextSecondary
        isSecondary -> IdeTextPrimary
        else -> androidx.compose.ui.graphics.Color.White
    }

    val borderColor = when {
        isSecondary -> IdeBorder
        else -> androidx.compose.ui.graphics.Color.Transparent
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .clickable(enabled = enabled && !isLoading) { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = textColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun IdeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = if (isFocused) IdeAccent else IdeBorder
    val backgroundColor = IdeSurface

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .padding(10.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = IdeTextPrimary),
        singleLine = true,
        cursorBrush = SolidColor(IdeTextPrimary),
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = IdeTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            innerTextField()
        }
    )
}
