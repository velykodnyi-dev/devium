package com.openclaw.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.theme.IdeBackground
import com.openclaw.mobile.theme.IdeBorder
import com.openclaw.mobile.theme.IdeTextPrimary

@Composable
fun IdeTitleBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().background(IdeBackground)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Slimmer than standard TopAppBar
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationIcon != null && onNavigationClick != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onNavigationClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate",
                        tint = IdeTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = IdeTextPrimary,
                modifier = Modifier.weight(1f).padding(start = if (navigationIcon != null) 4.dp else 0.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                actions()
            }
        }

        // 1dp IDE border separating title bar from content
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(IdeBorder))
    }
}
