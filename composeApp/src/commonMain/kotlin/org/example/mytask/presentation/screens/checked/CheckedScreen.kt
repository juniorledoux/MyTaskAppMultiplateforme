package org.example.mytask.presentation.screens.checked

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.mytask.presentation.components.TaskList
import org.example.mytask.presentation.screens.tasks.TaskUiState
import org.example.mytask.presentation.screens.tasks.TaskViewModel

@Composable
fun CheckedScreen(
    viewModel: TaskViewModel,
    taskUiState: TaskUiState,
    onNavigationRouteNameChange: (String) -> Unit,
    canNavigate: Boolean = true,
    modifier: Modifier = Modifier
) {
    TaskList(
        viewModel = viewModel,
        taskUiState = taskUiState,
        tasks = taskUiState.tasks.filter { task -> task.isCompleted },
        onNavigationRouteNameChange = onNavigationRouteNameChange,
        canNavigate=canNavigate
    )
}