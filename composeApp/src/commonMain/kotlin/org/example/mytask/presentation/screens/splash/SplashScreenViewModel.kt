package org.example.mytask.presentation.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.auth.FirebaseAuthException
import org.example.mytask.core.SnackBarController
import org.example.mytask.core.SnackBarEvent
import org.example.mytask.core.launchCatching
import org.example.mytask.domain.use_cases.AuthUseCase
import org.example.mytask.presentation.components.RouteNames

class SplashScreenViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val showError = mutableStateOf(false)

    fun onAppStart(navigate: (String) -> Unit) {
        showError.value = false
        if (authUseCase.hasUser) navigate(RouteNames.Tasks.route)
        else createAnonymousAccount(navigate)
    }

    private fun createAnonymousAccount(navigate: (String) -> Unit) {
        launchCatching(scope = viewModelScope) {
            try {
                authUseCase.loginAsAnonymous()
                SnackBarController.sendEvent(event = SnackBarEvent(message = "Welcome !"))
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                SnackBarController.sendEvent(event = SnackBarEvent(message = ex.message.toString()))
            }
            navigate(RouteNames.Tasks.route)
        }
    }
}