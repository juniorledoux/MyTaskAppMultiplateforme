package org.example.mytask.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val email: String = "",
    val password: String = "",
)
