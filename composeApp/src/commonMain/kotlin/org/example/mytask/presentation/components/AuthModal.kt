package org.example.mytask.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.mytask.presentation.screens.account.AccountUiState
import org.example.mytask.presentation.screens.account.AccountViewModel

@Composable
fun AuthModal(
    showDialog: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    toLogin: Boolean,
    viewModel: AccountViewModel,
    accountUiState: AccountUiState,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit,
    modifier: Modifier
) {

    val dialogOffset by animateFloatAsState(
        targetValue = if (showDialog) 0f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOutElastic
        )
    )

    AnimatedVisibility(
        visible = showDialog,
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            // Form content goes here
            Surface(
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().graphicsLayer {
                    translationY = dialogOffset * size.height // Apply vertical offset
                }.padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "Authentication", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.userToAuthenticated.value.email,
                        onValueChange = onEmailChange,
                        isError = accountUiState.currentTextFieldEmailError,
                        label = { Text("Email") },
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                    )
                    if (accountUiState.currentTextFieldEmailError) {
                        Text(
                            text = accountUiState.currentTextFieldEmailErrorMessage,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    OutlinedTextField(
                        value = viewModel.userToAuthenticated.value.password,
                        onValueChange = onPasswordChange,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = accountUiState.currentTextFieldPasswordError,
                        label = { Text("Password") },
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                    )
                    if (accountUiState.currentTextFieldPasswordError) {
                        Text(
                            text = accountUiState.currentTextFieldPasswordErrorMessage,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
                            onSubmit(
                                viewModel.userToAuthenticated.value.email,
                                viewModel.userToAuthenticated.value.password
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        if (accountUiState.isLoading)
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(32.dp),
                                strokeCap = StrokeCap.Round
                            ) else Text(if (toLogin) "Login now !" else "Register now !")
                    }
                }
            }
        }
    }
}
