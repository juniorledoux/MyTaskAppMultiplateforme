package org.example.mytask.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDestination
import org.example.mytask.domain.models.Task
import org.example.mytask.presentation.screens.tasks.TaskActions
import org.example.mytask.presentation.screens.tasks.TaskUiState

@Composable
fun AddTaskModal(
    showDialog: Boolean,
    task: Task,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    toEdit: Boolean,
    taskUiState: TaskUiState,
    currentDestination: NavDestination?,
    onDismiss: () -> Unit,
    onSubmit: (Task, TaskActions) -> Unit,
    modifier: Modifier
) {

    val dialogOffset by animateFloatAsState(
        targetValue = if (showDialog) 0f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOutElastic
        )
    )

    AnimatedVisibility(
        visible = showDialog,
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            // Form content goes here
            Surface(
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxSize().graphicsLayer {
                    translationX = dialogOffset * size.width // Apply horizontal offset
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "Add Task", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))


                    OutlinedTextField(
                        value = task.title,
                        onValueChange = onTitleChange,
                        isError = taskUiState.currentTextFieldTitleError,
                        label = { Text("Enter Title") },
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                    )
                    if (taskUiState.currentTextFieldTitleError) {
                        Text(
                            text = taskUiState.currentTextFieldTitleErrorMessage,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    OutlinedTextField(
                        value = task.description,
                        onValueChange = onDescriptionChange,
                        isError = taskUiState.currentTextFieldDescriptionError,
                        label = { Text("Enter Description") },
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        shape = MaterialTheme.shapes.medium,
                    )
                    if (taskUiState.currentTextFieldDescriptionError) {
                        Text(
                            text = taskUiState.currentTextFieldDescriptionErrorMessage,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onSubmit(
                                    if (toEdit) Task(
                                        id = task.id,
                                        title = task.title,
                                        description = task.description,
                                    ) else Task(
                                        title = task.title,
                                        description = task.description,
                                        isCompleted = currentDestination?.route == RouteNames.Checked.route,
                                        isFavorite = currentDestination?.route == RouteNames.Favorites.route,
                                    ),
                                    if (toEdit) TaskActions.EditTask else TaskActions.AddTask
                                )
                            },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(if (toEdit) "Edit" else "Add")
                        }
                    }

                }
            }
        }
    }
}
