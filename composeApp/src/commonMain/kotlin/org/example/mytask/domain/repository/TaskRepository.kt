package org.example.mytask.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.mytask.domain.models.Task

interface TaskRepository {
    fun getTasks(userId: String): Flow<List<Task>>
    fun getTaskById(id: String): Flow<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}