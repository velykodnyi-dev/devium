package com.openclaw.mobile.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.data.OpenClawSession
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeSurfaceCard
import com.openclaw.mobile.ui.components.IdeTitleBar

@Composable
fun SessionListScreen(onSessionClick: (String) -> Unit, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var sessions by remember { mutableStateOf<List<OpenClawSession>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        sessions = repository.getSessions()
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Agent Sessions",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sessions) { session ->
                    IdeSurfaceCard(onClick = { onSessionClick(session.id) }) {
                        Column {
                            Text(session.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = IdeTextPrimary)
                            Text("Status: ${session.status.name}", style = MaterialTheme.typography.bodyMedium, color = IdeInfo)
                            Spacer(Modifier.height(8.dp))
                            Text("Workspace: ${session.workspaceId}", style = MaterialTheme.typography.labelMedium, color = IdeTextSecondary)
                            Text("Updated: ${session.lastUpdated}", style = MaterialTheme.typography.labelSmall, color = IdeTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
