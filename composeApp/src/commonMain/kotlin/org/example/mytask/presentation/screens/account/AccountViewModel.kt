package org.example.mytask.presentation.screens.account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.mytask.core.SnackBarController
import org.example.mytask.core.SnackBarEvent
import org.example.mytask.core.launchCatching
import org.example.mytask.domain.models.User
import org.example.mytask.domain.use_cases.AuthUseCase

class AccountViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()
    val userToAuthenticated = mutableStateOf(User())


    init {
        getAuthUser()
    }

    private fun getAuthUser() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            authUseCase.currentUser.catch {
                _uiState.update {
                    it.copy(errorMessage = it.errorMessage, isLoading = false)
                }
            }.collect { user ->

                _uiState.update {
                    it.copy(
                        user = user,
                        currentUserId = user.id,
                        isAuthenticated = user.email.isNotEmpty() && user.email != "null",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEmailChange(newValue: String) {
        userToAuthenticated.value = userToAuthenticated.value.copy(email = newValue)
        _uiState.update {
            it.copy(
                currentTextFieldEmailError = false
            )
        }
    }

    fun onPasswordChange(newValue: String) {
        userToAuthenticated.value = userToAuthenticated.value.copy(password = newValue)
        _uiState.update {
            it.copy(
                currentTextFieldPasswordError = false
            )
        }
    }

    private fun isValideForm(): Boolean {
        var isValide = true
        val currentEmail = userToAuthenticated.value.email
        val currentPassword = userToAuthenticated.value.password

        if (currentEmail.isBlank() || !currentEmail.contains("@") || !currentEmail.contains(".")) {
            _uiState.update {
                it.copy(
                    currentTextFieldEmailError = true,
                    currentTextFieldEmailErrorMessage = "Enter a valid email address"
                )
            }
            isValide = false
        }
        if (currentPassword.isBlank() || currentPassword.length < 8) {
            _uiState.update {
                it.copy(
                    currentTextFieldPasswordError = true,
                    currentTextFieldPasswordErrorMessage = "The min length of password is 8"
                )
            }
            isValide = false
        }
        return isValide
    }

    fun onLogin(email: String, password: String, callback: () -> Unit) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        if (isValideForm()) launchCatching(scope = viewModelScope) {
            authUseCase.login(
                email,
                password
            )
            callback()
            SnackBarController.sendEvent(event = SnackBarEvent(message = "Login successfully"))
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onRegister(email: String, password: String, callback: () -> Unit) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        if (isValideForm()) launchCatching(scope = viewModelScope) {
            authUseCase.register(
                email,
                password
            )
            callback()
            SnackBarController.sendEvent(event = SnackBarEvent(message = "Register successfully"))
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onLogout() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        launchCatching(scope = viewModelScope) {
            authUseCase.logout()
            SnackBarController.sendEvent(event = SnackBarEvent(message = "Logout successfully"))
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}