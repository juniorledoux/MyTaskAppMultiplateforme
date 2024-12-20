package org.example.mytask.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.mytask.domain.models.Task
import org.example.mytask.presentation.screens.tasks.TaskActions
import org.example.mytask.presentation.screens.tasks.TaskUiState
import org.example.mytask.presentation.screens.tasks.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList(
    viewModel: TaskViewModel,
    taskUiState: TaskUiState,
    tasks: List<Task>,
    onNavigationRouteNameChange: (String) -> Unit,
    canNavigate: Boolean = true,
) {
    if (taskUiState.isLoading)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(64.dp),
                strokeCap = StrokeCap.Round
            )
        } else {

        var showDialog by remember { mutableStateOf(false) }
        var showBottomSheet by remember { mutableStateOf(false) }

        var selectedTask by remember { mutableStateOf(Task()) }
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        val taskToEdit by viewModel.taskToEdit

        BottomSheetLinks(
            taskViewModel = viewModel,
            selectedTask = selectedTask,
            isShow = showBottomSheet,
            onDismiss = {
                showBottomSheet = false
                scope.launch {
                    sheetState.hide()
                }
            },
            onEditClick = { currentTask ->
                viewModel.initTaskToEdit(currentTask)
                showDialog = true
            },
            sheetState = sheetState,
            scope = scope
        )


        AddTaskModal(
            showDialog = showDialog,
            task = taskToEdit,
            onTitleChange = viewModel::onTitleChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onDismiss = { showDialog = false },
            toEdit = true,
            taskUiState = taskUiState,
            currentDestination = null,
            onSubmit = { task, action ->
                viewModel.onTaskAction(action, task, callback = {
                    showDialog =
                        false
                })
            },
            modifier = Modifier.fillMaxSize()
        )
        if (tasks.isEmpty()) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No tasks registered...")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
            ) {
                items(tasks) { task ->
                    TaskCard(task = task,
                        onCardClick = { selected ->
                            if (canNavigate) onNavigationRouteNameChange("${RouteNames.TasksDetails.route}/${selected.id}")
                            viewModel.setSelectedTask(selected.id)
                        }, onMoreClick = {
                            showBottomSheet = true
                            selectedTask = it
                            scope.launch {
                                sheetState.show()
                            }
                        }, onFavoriteClick = {
                            if (it.isFavorite) viewModel.onTaskAction(
                                TaskActions.UnmarkAsFavorite,
                                it
                            ) else viewModel.onTaskAction(TaskActions.MarkAsFavorite, it)
                        })
                }
            }
        }
    }
}

