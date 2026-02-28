package com.openclaw.mobile.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeButton
import com.openclaw.mobile.ui.components.IdeTitleBar

@Composable
fun SettingsScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    var biometricEnabled by remember { mutableStateOf(false) }
    var wrapLines by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Settings",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Preferences", style = MaterialTheme.typography.titleLarge, color = IdeTextPrimary)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Biometric Unlock", color = IdeTextPrimary)
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = IdeBackground,
                        checkedTrackColor = IdeAccent,
                        uncheckedThumbColor = IdeTextSecondary,
                        uncheckedTrackColor = IdeSurface
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Wrap Lines in Code Viewer", color = IdeTextPrimary)
                Switch(
                    checked = wrapLines,
                    onCheckedChange = { wrapLines = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = IdeBackground,
                        checkedTrackColor = IdeAccent,
                        uncheckedThumbColor = IdeTextSecondary,
                        uncheckedTrackColor = IdeSurface
                    )
                )
            }

            Spacer(Modifier.height(32.dp))
            Text("Connection", style = MaterialTheme.typography.titleLarge, color = IdeTextPrimary)

            IdeButton(
                text = "Logout",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                isDestructive = true
            )
        }
    }
}
