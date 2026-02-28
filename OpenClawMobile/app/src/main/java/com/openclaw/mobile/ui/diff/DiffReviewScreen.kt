package com.openclaw.mobile.ui.diff

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.ChangedFile
import com.openclaw.mobile.data.DiffHunk
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeButton
import com.openclaw.mobile.ui.components.IdeSurfaceCard
import com.openclaw.mobile.ui.components.IdeTitleBar

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

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Review Changes",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack
        )

        if (isLoading) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(diffs) { file ->
                    DiffFileItem(file)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(IdeSurface)
                .border(1.dp, IdeBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IdeButton(
                    text = "Approve",
                    onClick = { /* Approve */ },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                IdeButton(
                    text = "Reject",
                    onClick = { /* Reject */ },
                    modifier = Modifier.weight(1f),
                    isDestructive = true
                )
            }
        }
    }
}

@Composable
fun DiffFileItem(file: ChangedFile) {
    IdeSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(file.name, style = MaterialTheme.typography.titleMedium, color = IdeTextPrimary)
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
            .border(1.dp, IdeBorder, Shapes.small)
            .background(IdeBackground)
            .padding(8.dp)
    ) {
        Text(
            text = "@@ -${hunk.oldStart},${hunk.oldLines} +${hunk.newStart},${hunk.newLines} @@",
            style = MaterialTheme.typography.labelSmall,
            color = IdeTextSecondary,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        hunk.content.split("\n").filter { !it.startsWith("@@") }.forEach { line ->
            val color = when {
                line.startsWith("+") -> IdeAddedText
                line.startsWith("-") -> IdeDeletedText
                else -> IdeTextPrimary
            }
            val bgColor = when {
                line.startsWith("+") -> IdeAddedBackground
                line.startsWith("-") -> IdeDeletedBackground
                else -> Color.Transparent
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
