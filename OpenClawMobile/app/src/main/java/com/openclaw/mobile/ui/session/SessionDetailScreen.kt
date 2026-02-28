package com.openclaw.mobile.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.openclaw.mobile.ui.components.IdeButton
import com.openclaw.mobile.ui.components.IdeSurfaceCard
import com.openclaw.mobile.ui.components.IdeTextField
import com.openclaw.mobile.ui.components.IdeTitleBar

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

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Session: ${session?.title ?: "..."}",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack,
            actions = {
                IconButton(onClick = onViewDiff) {
                    Icon(Icons.Default.RateReview, contentDescription = "Review Diff", tint = IdeTextPrimary)
                }
            }
        )

        if (isLoading) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Info Header
                IdeSurfaceCard(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text("Status: ${session?.status?.name ?: "UNKNOWN"}", color = IdeInfo)
                        Spacer(Modifier.height(4.dp))
                        Text("Updated: ${session?.lastUpdated ?: "UNKNOWN"}", color = IdeTextSecondary)
                    }
                }

                // Terminal/Log Output
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(1.dp, IdeBorder, Shapes.small)
                        .background(IdeBackground)
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

        // Simulated chat / instruction input box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(IdeSurface)
                .border(1.dp, IdeBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var text by remember { mutableStateOf("") }
                IdeTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = "Send instruction to agent..."
                )
                Spacer(Modifier.width(12.dp))
                IdeButton(
                    text = "Send",
                    onClick = { /* Send mock instruction */ }
                )
            }
        }
    }
}

@Composable
fun LogLine(log: SessionLog) {
    val color = when (log.type) {
        LogType.INFO -> IdeTextSecondary
        LogType.AGENT -> IdeAccent
        LogType.COMMAND -> CodeFunction
        LogType.ERROR -> IdeError
    }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = "[${log.timestamp}] ",
            color = IdeBorderLight,
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
