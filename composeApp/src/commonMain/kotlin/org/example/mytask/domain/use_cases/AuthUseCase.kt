package org.example.mytask.domain.use_cases

import org.example.mytask.domain.repository.AuthRepository

class AuthUseCase(private val authRepository: AuthRepository) {

    val currentUser get() = authRepository.currentUser

    val hasUser get() = authRepository.hasUser

    val isAuthenticated get() = authRepository.isAuthenticated

    val currentUserId get() = authRepository.currentUserId

    suspend fun loginAsAnonymous() =
        authRepository.loginAsAnonymous()

    suspend fun login(email: String, password: String) =
        authRepository.login(email, password)

    suspend fun register(email: String, password: String) =
        authRepository.register(email, password)

    suspend fun logout() =
        authRepository.logout()

}