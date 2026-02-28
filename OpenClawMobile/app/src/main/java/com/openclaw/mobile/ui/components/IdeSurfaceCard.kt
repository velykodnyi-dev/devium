package com.openclaw.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.theme.IdeBorder
import com.openclaw.mobile.theme.IdeSurface

@Composable
fun IdeSurfaceCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    var baseModifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp))
        .background(IdeSurface)
        .border(1.dp, IdeBorder, RoundedCornerShape(4.dp))

    if (onClick != null) {
        baseModifier = baseModifier.clickable { onClick() }
    }

    Box(
        modifier = baseModifier.padding(12.dp),
        content = content
    )
}
