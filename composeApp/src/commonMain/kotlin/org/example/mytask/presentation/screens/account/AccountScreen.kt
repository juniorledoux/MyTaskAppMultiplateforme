package org.example.mytask.presentation.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.mytask.presentation.components.AuthModal
import org.example.mytask.presentation.components.RouteNames
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = koinViewModel(),
    onNavigationRouteNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accountUiState by accountViewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var toLogin by remember { mutableStateOf(true) }

    //My add task component
    AuthModal(
        accountUiState = accountUiState,
        viewModel = accountViewModel,
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        toLogin = toLogin,
        onSubmit = { email, password ->
            if (toLogin) {
                accountViewModel.onLogin(email, password, {
                    showDialog = false
                })
            } else {
                accountViewModel.onRegister(email, password, {
                    showDialog = false
                })
            }
        },
        modifier = Modifier.fillMaxSize(),
        onEmailChange = { accountViewModel.onEmailChange(it) },
        onPasswordChange = { accountViewModel.onPasswordChange(it) },
    )

    Column(
        modifier = modifier
            .padding(16.dp).verticalScroll(
                state = rememberScrollState(),
            ).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (accountUiState.isAuthenticated) {
            Text(
                text = accountUiState.user.email,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = {
                    accountViewModel.onLogout()
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "logout"
                )
            }
        } else {
            Text(
                text = "You're authenticated as anonymous. Please login or register your account to continue and make safe your tasks.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedButton(
                onClick = {
                    toLogin = true
                    showDialog = true
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Login"
                )
            }
            Button(
                onClick = {
                    toLogin = false
                    showDialog = true
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Register"
                )
            }
        }

    }
}