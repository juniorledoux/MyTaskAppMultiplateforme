package org.example.mytask.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val isFavorite: Boolean = false,
    val userId: String = "",
)
