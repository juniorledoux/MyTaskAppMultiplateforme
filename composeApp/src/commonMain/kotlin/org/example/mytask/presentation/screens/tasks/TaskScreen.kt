package org.example.mytask.presentation.screens.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.mytask.presentation.components.TaskList


@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    taskUiState: TaskUiState,
    onNavigationRouteNameChange: (String) -> Unit,
    canNavigate: Boolean = true,
    modifier: Modifier = Modifier
) {
    TaskList(
        viewModel = viewModel,
        taskUiState = taskUiState,
        tasks = taskUiState.tasks,
        onNavigationRouteNameChange = onNavigationRouteNameChange,
        canNavigate=canNavigate
    )
}

