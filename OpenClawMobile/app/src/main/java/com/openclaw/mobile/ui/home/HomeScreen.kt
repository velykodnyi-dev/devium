package com.openclaw.mobile.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.data.OpenClawSession
import com.openclaw.mobile.data.Workspace
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeSurfaceCard
import com.openclaw.mobile.ui.components.IdeTitleBar

@Composable
fun HomeScreen(
    onNavigateToWorkspaces: () -> Unit,
    onNavigateToSessions: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSessionClick: (String) -> Unit
) {
    val repository = remember { MockBackendRepository() }
    var workspaces by remember { mutableStateOf<List<Workspace>>(emptyList()) }
    var sessions by remember { mutableStateOf<List<OpenClawSession>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        workspaces = repository.getWorkspaces().take(3)
        sessions = repository.getSessions().take(3)
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "OpenClaw",
            actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = IdeTextPrimary)
                }
            }
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Connection Status: Connected",
                        color = IdeAddedText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item { Spacer(Modifier.height(8.dp)) }

                item {
                    SectionHeader(
                        title = "Recent Workspaces",
                        onSeeAll = onNavigateToWorkspaces
                    )
                }
                items(workspaces) { workspace ->
                    IdeSurfaceCard(onClick = { onNavigateToWorkspaces() }) {
                        Column {
                            Text(workspace.name, fontWeight = FontWeight.Bold, color = IdeTextPrimary)
                            Text(workspace.path, style = MaterialTheme.typography.bodySmall, color = IdeTextSecondary)
                            Spacer(Modifier.height(8.dp))
                            Text("Accessed ${workspace.lastAccessed}", style = MaterialTheme.typography.labelSmall, color = IdeTextSecondary)
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) }

                item {
                    SectionHeader(
                        title = "Active Sessions",
                        onSeeAll = onNavigateToSessions
                    )
                }
                items(sessions) { session ->
                    IdeSurfaceCard(onClick = { onSessionClick(session.id) }) {
                        Column {
                            Text(session.title, fontWeight = FontWeight.Bold, color = IdeTextPrimary)
                            Text("Status: ${session.status.name}", style = MaterialTheme.typography.bodySmall, color = IdeInfo)
                            Spacer(Modifier.height(8.dp))
                            Text("Updated ${session.lastUpdated}", style = MaterialTheme.typography.labelSmall, color = IdeTextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = IdeTextPrimary)
        Text(
            text = "See All",
            color = IdeAccent,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { onSeeAll() }.padding(8.dp)
        )
    }
}
