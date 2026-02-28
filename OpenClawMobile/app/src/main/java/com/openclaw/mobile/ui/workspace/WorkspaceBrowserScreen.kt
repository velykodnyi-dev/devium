package com.openclaw.mobile.ui.workspace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.FileNode
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.DarkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceBrowserScreen(workspaceId: String, onFileClick: (String) -> Unit, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var fileTree by remember { mutableStateOf<FileNode?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(workspaceId) {
        isLoading = true
        fileTree = repository.getFileTree(workspaceId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workspace Files", color = DarkPrimary) },
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
            fileTree?.let { rootNode ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        FileTreeItem(node = rootNode, level = 0, onFileClick = onFileClick)
                    }
                }
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Could not load workspace")
                }
            }
        }
    }
}

@Composable
fun FileTreeItem(node: FileNode, level: Int, onFileClick: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(level == 0) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (node.isDirectory) {
                        isExpanded = !isExpanded
                    } else {
                        onFileClick(node.id)
                    }
                }
                .padding(start = (level * 16).dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (node.isDirectory) Icons.Default.Folder else Icons.Default.Description,
                contentDescription = null,
                tint = if (node.isDirectory) DarkPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (isExpanded && node.isDirectory && node.children != null) {
            node.children.forEach { child ->
                FileTreeItem(node = child, level = level + 1, onFileClick = onFileClick)
            }
        }
    }
}
