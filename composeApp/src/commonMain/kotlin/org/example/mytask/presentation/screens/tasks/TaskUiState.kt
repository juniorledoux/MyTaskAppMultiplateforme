package org.example.mytask.presentation.screens.tasks

import org.example.mytask.domain.models.Task

data class TaskUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,

    val isShowAddTaskDialog: Boolean = false,
    val isShowMoreOptionBottomDialog: Boolean = false,
    val isShowUpdateTaskDialog: Boolean = false,
    val isShowDeleteTaskDialog: Boolean = false,

    val currentTextFieldTitleError: Boolean = false,
    val currentTextFieldTitleErrorMessage: String = "",
    val currentTextFieldDescriptionError: Boolean = false,
    val currentTextFieldDescriptionErrorMessage: String = "",

    val selectedTask: Task = Task(),
)
