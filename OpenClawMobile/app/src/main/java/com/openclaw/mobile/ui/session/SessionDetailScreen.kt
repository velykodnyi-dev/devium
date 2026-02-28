package com.openclaw.mobile.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.LogType
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.data.OpenClawSession
import com.openclaw.mobile.data.SessionLog
import com.openclaw.mobile.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(sessionId: String, onViewDiff: () -> Unit, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var session by remember { mutableStateOf<OpenClawSession?>(null) }
    var logs by remember { mutableStateOf<List<SessionLog>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    LaunchedEffect(sessionId) {
        isLoading = true
        session = repository.getSessions().find { it.id == sessionId }
        logs = repository.getSessionLogs(sessionId)
        isLoading = false
    }

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Session: ${session?.title ?: "..."}", color = DarkPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onViewDiff) {
                        Icon(Icons.Default.RateReview, contentDescription = "Review Diff")
                    }
                }
            )
        },
        bottomBar = {
            // Simulated chat / instruction input box
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Send instruction to agent...") },
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { /* Send mock instruction */ }) {
                        Text("Send")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Info Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Status: ${session?.status?.name ?: "UNKNOWN"}", color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(4.dp))
                        Text("Updated: ${session?.lastUpdated ?: "UNKNOWN"}")
                    }
                }

                // Terminal/Log Output
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(CodeBackground, Shapes.medium)
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(logs) { log ->
                            LogLine(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogLine(log: SessionLog) {
    val color = when (log.type) {
        LogType.INFO -> TextSecondary
        LogType.AGENT -> DarkPrimary
        LogType.COMMAND -> CodeFunction
        LogType.ERROR -> DarkError
    }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = "[${log.timestamp}] ",
            color = Color.DarkGray,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = log.message,
            color = color,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
