package org.example.mytask.presentation.screens.account

import org.example.mytask.domain.models.User

data class AccountUiState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val currentUserId: String = "",
    val isAuthenticated: Boolean = false,
    val errorMessage: String = "",

    val currentTextFieldEmailError: Boolean = false,
    val currentTextFieldEmailErrorMessage: String = "",
    val currentTextFieldPasswordError: Boolean = false,
    val currentTextFieldPasswordErrorMessage: String = "",
)
