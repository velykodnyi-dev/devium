package com.openclaw.mobile.ui.home

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
import com.openclaw.mobile.theme.StatusAdded
import com.openclaw.mobile.theme.DarkPrimary

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OpenClaw", color = DarkPrimary) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Connection Status: Connected",
                        color = StatusAdded,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }

                item {
                    SectionHeader(
                        title = "Recent Workspaces",
                        onSeeAll = onNavigateToWorkspaces
                    )
                }
                items(workspaces) { workspace ->
                    WorkspaceCard(workspace) {
                        // In a real app we would navigate to the specific workspace browser
                        onNavigateToWorkspaces()
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }

                item {
                    SectionHeader(
                        title = "Active Sessions",
                        onSeeAll = onNavigateToSessions
                    )
                }
                items(sessions) { session ->
                    SessionCard(session) {
                        onSessionClick(session.id)
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
        Text(title, style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onSeeAll) {
            Text("See All", color = DarkPrimary)
        }
    }
}

@Composable
fun WorkspaceCard(workspace: Workspace, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(workspace.name, fontWeight = FontWeight.Bold)
            Text(workspace.path, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text("Accessed ${workspace.lastAccessed}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SessionCard(session: OpenClawSession, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(session.title, fontWeight = FontWeight.Bold)
            Text("Status: ${session.status.name}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text("Updated ${session.lastUpdated}", style = MaterialTheme.typography.labelSmall)
        }
    }
}
