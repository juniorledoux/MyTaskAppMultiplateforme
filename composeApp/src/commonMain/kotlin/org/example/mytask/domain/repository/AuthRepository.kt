package org.example.mytask.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.mytask.domain.models.User

interface AuthRepository {
    val currentUserId: String

    val isAuthenticated: Boolean

    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun loginAsAnonymous()

    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String)

    suspend fun logout()
}