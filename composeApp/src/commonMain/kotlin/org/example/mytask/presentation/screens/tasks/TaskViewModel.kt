package org.example.mytask.presentation.screens.tasks

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
import org.example.mytask.domain.models.Task
import org.example.mytask.domain.models.User
import org.example.mytask.domain.use_cases.AuthUseCase
import org.example.mytask.domain.use_cases.TaskUseCase

class TaskViewModel(private val taskUseCase: TaskUseCase, private val authUseCase: AuthUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    val authUser = mutableStateOf(User())
    val taskToEdit = mutableStateOf(Task())

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
                authUser.value = authUser.value.copy(
                    id = user.id,
                    email = user.email,
                    isAnonymous = user.isAnonymous,
                )
                getAllTasks(user.id)
            }
        }
    }

    private fun getAllTasks(userId: String) {
        viewModelScope.launch {
            taskUseCase.getTasks(userId).catch {
                _uiState.update {
                    it.copy(errorMessage = it.errorMessage, isLoading = false)
                }
            }.collect { tasks ->
                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false,
                        selectedTask = tasks.find { task -> task.id == it.selectedTask.id }
                            ?: Task())
                }
            }
        }
    }

    fun onTitleChange(newValue: String) {
        taskToEdit.value = taskToEdit.value.copy(title = newValue)
        _uiState.update {
            it.copy(
                currentTextFieldTitleError = false
            )
        }
    }

    fun initTaskToEdit(newValue: Task) {
        taskToEdit.value = taskToEdit.value.copy(
            id = newValue.id,
            title = newValue.title,
            description = newValue.description,
            isCompleted = newValue.isCompleted,
            isFavorite = newValue.isFavorite, userId = newValue.userId
        )

    }

    fun onDescriptionChange(newValue: String) {
        taskToEdit.value = taskToEdit.value.copy(description = newValue)
        _uiState.update {
            it.copy(
                currentTextFieldDescriptionError = false
            )
        }
    }


    fun setSelectedTask(taskId: String) {
        _uiState.update {
            it.copy(selectedTask = it.tasks.find { task -> task.id == taskId } ?: Task())
        }

        /*
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            taskUseCase.getTaskById(taskId).catch {
                _uiState.update { it.copy(errorMessage = it.errorMessage, isLoading = false) }
            }.collect { task ->
                _uiState.update { it.copy(selectedTask = task, isLoading = false) }
            }
        }
        */
    }


    private fun isValideForm(): Boolean {
        var isValide = true
        val currentTitle = taskToEdit.value.title
        val currentDescription = taskToEdit.value.description

        if (currentTitle.isBlank() || currentTitle.length < 3) {
            _uiState.update {
                it.copy(
                    currentTextFieldTitleError = true,
                    currentTextFieldTitleErrorMessage = "The min length of title is 3"
                )
            }
            isValide = false
        }
        if (currentDescription.isBlank() || currentDescription.length < 10) {
            _uiState.update {
                it.copy(
                    currentTextFieldDescriptionError = true,
                    currentTextFieldDescriptionErrorMessage = "The min length of description is 10"
                )
            }
            isValide = false
        }
        return isValide
    }

    fun onTaskAction(actions: TaskActions, task: Task, callback: () -> Unit = {}) {
        when (actions) {
            TaskActions.AddTask -> onAddTaskClick(task, callback)
            TaskActions.EditTask -> onEditTaskClick(task, callback)
            TaskActions.MarkAsCompleted -> onMarkOrUnmarkAsCompletedClick(task, true)
            TaskActions.UnMarkAsCompleted -> onMarkOrUnmarkAsCompletedClick(task, false)
            TaskActions.MarkAsFavorite -> onMarkOrUnmarkAsFavoriteClick(task, true)
            TaskActions.UnmarkAsFavorite -> onMarkOrUnmarkAsFavoriteClick(task, false)
            TaskActions.DeleteTask -> onDeleteTaskClick(task)
        }
    }

    private fun onAddTaskClick(task: Task, callback: () -> Unit) {
        val taskUser = task.copy(userId = authUser.value.id)
        if (isValideForm()) launchCatching(scope = viewModelScope) {
            taskUseCase.addTask(taskUser)
            callback()
            SnackBarController.sendEvent(event = SnackBarEvent(message = "New task added successfully"))
        }
    }

    private fun onEditTaskClick(task: Task, callback: () -> Unit) {
        if (isValideForm()) launchCatching(scope = viewModelScope) {
            taskUseCase.updateTask(task)
            callback()
            SnackBarController.sendEvent(event = SnackBarEvent(message = "Task edited successfully"))
        }
    }

    private fun onMarkOrUnmarkAsCompletedClick(task: Task, isCompleted: Boolean) {
        val completedTask = task.copy(isCompleted = isCompleted)
        launchCatching(scope = viewModelScope) {
            taskUseCase.updateTask(completedTask)
            SnackBarController.sendEvent(event = SnackBarEvent(message = if (isCompleted) "Task marked as completed successfully" else "Un marked task as completed successfully"))
        }
    }

    private fun onMarkOrUnmarkAsFavoriteClick(task: Task, isFavorite: Boolean) {
        val favoriteTask = task.copy(isFavorite = isFavorite)
        launchCatching(scope = viewModelScope) {
            taskUseCase.updateTask(favoriteTask)
            SnackBarController.sendEvent(event = SnackBarEvent(message = if (isFavorite) "Task marked as favorite successfully" else "Un marked task as favorite successfully"))
        }
    }

    private fun onDeleteTaskClick(task: Task) {
        launchCatching(scope = viewModelScope) {
            taskUseCase.deleteTask(task)
            SnackBarController.sendEvent(event = SnackBarEvent(message = "Task was deleted successfully"))
        }
    }

}