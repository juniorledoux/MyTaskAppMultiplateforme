package org.example.mytask.presentation.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.mytask.presentation.components.TaskList
import org.example.mytask.presentation.screens.tasks.TaskUiState
import org.example.mytask.presentation.screens.tasks.TaskViewModel


@Composable
fun FavoritesScreen(
    viewModel: TaskViewModel,
    taskUiState: TaskUiState,
    onNavigationRouteNameChange: (String) -> Unit,
    canNavigate: Boolean = true,
    modifier: Modifier = Modifier
) {
    TaskList(
        viewModel = viewModel,
        taskUiState = taskUiState,
        tasks = taskUiState.tasks.filter { task -> task.isFavorite },
        onNavigationRouteNameChange = onNavigationRouteNameChange,
        canNavigate=canNavigate
    )
}