package com.openclaw.mobile.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.openclaw.mobile.theme.*
import com.openclaw.mobile.ui.components.IdeButton
import com.openclaw.mobile.ui.components.IdeTextField

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var endpoint by remember { mutableStateOf("https://api.openclaw.dev") }
    var token by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IdeBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OpenClaw Mobile",
            style = MaterialTheme.typography.titleLarge,
            color = IdeTextPrimary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        IdeTextField(
            value = endpoint,
            onValueChange = { endpoint = it },
            placeholder = "Backend Endpoint"
        )

        Spacer(modifier = Modifier.height(16.dp))

        IdeTextField(
            value = token,
            onValueChange = { token = it },
            placeholder = "Access Token",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        IdeButton(
            text = "Connect",
            onClick = {
                isLoading = true
                coroutineScope.launch {
                    delay(1000)
                    isLoading = false
                    onLoginSuccess()
                }
            },
            enabled = token.isNotEmpty() && endpoint.isNotEmpty(),
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )
    }
}
