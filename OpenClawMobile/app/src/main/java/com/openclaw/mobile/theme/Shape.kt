package com.openclaw.mobile.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// IDEs typically have flatter UIs with sharper corners than material design default shapes
val Shapes = Shapes(
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(8.dp)
)
