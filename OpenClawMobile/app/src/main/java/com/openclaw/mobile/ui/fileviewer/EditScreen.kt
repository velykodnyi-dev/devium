package com.openclaw.mobile.ui.fileviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.openclaw.mobile.data.MockBackendRepository
import com.openclaw.mobile.theme.IdeAccent
import com.openclaw.mobile.theme.IdeBackground
import com.openclaw.mobile.theme.CodeTypography
import com.openclaw.mobile.theme.IdeTextPrimary
import com.openclaw.mobile.ui.components.IdeTitleBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(fileId: String, onBack: () -> Unit) {
    val repository = remember { MockBackendRepository() }
    var fileContent by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(fileId) {
        fileContent = repository.getFileContent(fileId)
    }

    Column(modifier = Modifier.fillMaxSize().background(IdeBackground)) {
        IdeTitleBar(
            title = "Edit File",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBack,
            actions = {
                IconButton(onClick = {
                    isSaving = true
                    // Simulate save
                    onBack()
                }) {
                    Icon(Icons.Default.Save, contentDescription = "Save", tint = IdeTextPrimary)
                }
            }
        )

        if (isSaving) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IdeAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TextField(
                    value = fileContent,
                    onValueChange = { fileContent = it },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = CodeTypography.copy(color = IdeTextPrimary),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = IdeBackground,
                        unfocusedContainerColor = IdeBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
