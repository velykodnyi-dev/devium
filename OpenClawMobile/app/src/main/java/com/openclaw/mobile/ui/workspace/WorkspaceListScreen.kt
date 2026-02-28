package com.openclaw.mobile.ui.workspace

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
import com.openclaw.mobile.theme.DarkPrimary

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workspaces", color = DarkPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(workspaces) { workspace ->
                    WorkspaceItem(workspace) {
                        onWorkspaceClick(workspace.id)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkspaceItem(workspace: Workspace, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(workspace.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(workspace.path, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text("Last accessed: ${workspace.lastAccessed}", style = MaterialTheme.typography.labelMedium)
        }
    }
}
