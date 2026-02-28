package com.openclaw.mobile.ui.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.FileNode
import com.openclaw.mobile.data.FileStatus
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeTitleBar

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

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "EXPLORER",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            fileTree?.let { rootNode ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        FileTreeItem(node = rootNode, level = 0, onFileClick = onFileClick)
                    }
                }
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Could not load workspace", color = IdeTextPrimary)
                }
            }
        }
    }
}

@Composable
fun FileTreeItem(node: FileNode, level: Int, onFileClick: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(level == 0) }
    var isHovered by remember { mutableStateOf(false) } // Android doesn't really have hover, but we'll use click states

    val textColor = when (node.status) {
        FileStatus.MODIFIED -> IdeModifiedText
        FileStatus.UNTRACKED, FileStatus.ADDED -> IdeAddedText
        FileStatus.DELETED -> IdeDeletedText
        FileStatus.UNMODIFIED -> IdeTextPrimary
    }

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
                .padding(start = (level * 16 + 8).dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (node.isDirectory) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = IdeTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                    contentDescription = null,
                    tint = IdeTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.padding(start = 4.dp)
            )

            if (node.status != FileStatus.UNMODIFIED) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = when(node.status) {
                        FileStatus.MODIFIED -> "M"
                        FileStatus.UNTRACKED, FileStatus.ADDED -> "U"
                        FileStatus.DELETED -> "D"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }

        if (isExpanded && node.isDirectory && node.children != null) {
            node.children.forEach { child ->
                FileTreeItem(node = child, level = level + 1, onFileClick = onFileClick)
            }
        }
    }
}
