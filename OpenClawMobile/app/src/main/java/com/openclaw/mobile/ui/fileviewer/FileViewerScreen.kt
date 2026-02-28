package com.openclaw.mobile.ui.fileviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.Diagnostic
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeTitleBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileViewerScreen(fileId: String, onEditClick: () -> Unit, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var fileContent by remember { mutableStateOf("") }
    var diagnostics by remember { mutableStateOf<List<Diagnostic>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState()
    var showIntelligenceSheet by remember { mutableStateOf(false) }

    LaunchedEffect(fileId) {
        isLoading = true
        fileContent = repository.getFileContent(fileId)
        diagnostics = repository.getDiagnostics(fileId)
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = if (fileId == "f1") "main.py" else "File",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack,
            actions = {
                IconButton(onClick = { showIntelligenceSheet = true }) {
                    Icon(Icons.Default.Info, contentDescription = "Intelligence", tint = IdeTextPrimary)
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = IdeTextPrimary)
                }
            }
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IdeBackground)
            ) {
                CodeViewer(content = fileContent)
            }
        }
    }

    if (showIntelligenceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showIntelligenceSheet = false },
            sheetState = sheetState,
            containerColor = IdeSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Code Intelligence", style = MaterialTheme.typography.titleLarge, color = IdeTextPrimary)
                Spacer(Modifier.height(16.dp))
                if (diagnostics.isEmpty()) {
                    Text("No diagnostics available.", color = IdeTextSecondary)
                } else {
                    diagnostics.forEach { diag ->
                        Text("Line ${diag.line}: ${diag.message}", color = IdeError)
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CodeViewer(content: String) {
    val lines = content.split("\n")
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
    ) {
        // Line numbers
        Column(
            modifier = Modifier
                .width(48.dp)
                .background(IdeBackground)
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.End
        ) {
            lines.forEachIndexed { index, _ ->
                Text(
                    text = (index + 1).toString(),
                    color = IdeTextSecondary,
                    style = CodeTypography
                )
            }
        }

        // Code content
        Box(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(horizontalScroll)
                .padding(8.dp)
        ) {
            Text(
                text = highlightSyntax(content),
                style = CodeTypography
            )
        }
    }
}

// Extremely simplified mock syntax highlighter for MVP purposes
fun highlightSyntax(code: String) = buildAnnotatedString {
    val keywords = listOf("def", "return", "if", "for", "in", "class", "pass")
    val functions = listOf("print", "range", "int")

    val words = code.split(Regex("(?<=\\s)|(?=\\s)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))|(?<=:)|(?=:)"))

    words.forEach { word ->
        when {
            word.trim() in keywords -> {
                withStyle(SpanStyle(color = CodeKeyword)) { append(word) }
            }
            word.trim() in functions -> {
                withStyle(SpanStyle(color = CodeFunction)) { append(word) }
            }
            word.startsWith("\"") || word.startsWith("'") -> {
                withStyle(SpanStyle(color = CodeString)) { append(word) }
            }
            word.trim().toLongOrNull() != null -> {
                withStyle(SpanStyle(color = CodeNumber)) { append(word) }
            }
            word == "True" || word == "False" -> {
                withStyle(SpanStyle(color = CodeKeyword)) { append(word) }
            }
            else -> {
                withStyle(SpanStyle(color = IdeTextPrimary)) { append(word) }
            }
        }
    }
}
