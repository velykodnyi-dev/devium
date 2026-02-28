package com.openclaw.mobile.ui.diff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.ChangedFile
import com.openclaw.mobile.data.DiffHunk
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiffReviewScreen(sessionId: String, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var diffs by remember { mutableStateOf<List<ChangedFile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(sessionId) {
        isLoading = true
        diffs = repository.getDiff(sessionId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Changes", color = DarkPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* Approve */ },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusAdded)
                    ) {
                        Text("Approve")
                    }
                    Button(
                        onClick = { /* Reject */ },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkError)
                    ) {
                        Text("Reject")
                    }
                }
            }
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
                items(diffs) { file ->
                    DiffFileItem(file)
                }
            }
        }
    }
}

@Composable
fun DiffFileItem(file: ChangedFile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(file.name, style = MaterialTheme.typography.titleMedium, color = DarkPrimary)
            Spacer(Modifier.height(8.dp))
            file.diffHunks.forEach { hunk ->
                DiffHunkViewer(hunk)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DiffHunkViewer(hunk: DiffHunk) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeBackground, Shapes.small)
            .padding(8.dp)
    ) {
        Text(
            text = "@@ -${hunk.oldStart},${hunk.oldLines} +${hunk.newStart},${hunk.newLines} @@",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        hunk.content.split("\n").filter { !it.startsWith("@@") }.forEach { line ->
            val color = when {
                line.startsWith("+") -> StatusAdded
                line.startsWith("-") -> StatusDeleted
                else -> CodeText
            }
            val bgColor = when {
                line.startsWith("+") -> StatusAdded.copy(alpha = 0.1f)
                line.startsWith("-") -> StatusDeleted.copy(alpha = 0.1f)
                else -> androidx.compose.ui.graphics.Color.Transparent
            }
            Text(
                text = line,
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}
