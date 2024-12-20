package org.example.mytask.domain.use_cases

import org.example.mytask.domain.models.Task
import org.example.mytask.domain.repository.TaskRepository

class TaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun getTasks(userId: String) = taskRepository.getTasks(userId)
    fun getTaskById(id: String) = taskRepository.getTaskById(id)
    suspend fun addTask(task: Task) = taskRepository.addTask(task)
    suspend fun updateTask(task: Task) = taskRepository.updateTask(task)
    suspend fun deleteTask(task: Task) = taskRepository.deleteTask(task)
}