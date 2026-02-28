package com.openclaw.mobile.ui.workspace

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
import com.openclaw.mobile.data.Workspace
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeSurfaceCard
import com.openclaw.mobile.ui.components.IdeTitleBar

@Composable
fun WorkspaceListScreen(onWorkspaceClick: (String) -> Unit, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var workspaces by remember { mutableStateOf<List<Workspace>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        workspaces = repository.getWorkspaces()
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Workspaces",
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
                items(workspaces) { workspace ->
                    IdeSurfaceCard(onClick = { onWorkspaceClick(workspace.id) }) {
                        Column {
                            Text(workspace.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = IdeTextPrimary)
                            Text(workspace.path, style = MaterialTheme.typography.bodyMedium, color = IdeTextSecondary)
                            Spacer(Modifier.height(8.dp))
                            Text("Last accessed: ${workspace.lastAccessed}", style = MaterialTheme.typography.labelMedium, color = IdeTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
