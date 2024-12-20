package org.example.mytask.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import org.example.mytask.domain.models.Task
import org.example.mytask.presentation.screens.tasks.TaskActions
import org.example.mytask.presentation.screens.tasks.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLinks(
    taskViewModel: TaskViewModel,
    selectedTask: Task,
    isShow: Boolean,
    onDismiss: () -> Unit,
    onEditClick: (Task) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(visible = isShow) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onDismiss()
            },
            modifier = Modifier.zIndex(0f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(8.dp))
                if (selectedTask.isCompleted) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                taskViewModel.onTaskAction(
                                    TaskActions.UnMarkAsCompleted,
                                    selectedTask
                                )
                                onDismiss()
                            }
                            .clip(MaterialTheme.shapes.small)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.TwoTone.Close,
                            null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Unmark as completed")
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                taskViewModel.onTaskAction(
                                    TaskActions.MarkAsCompleted,
                                    selectedTask
                                )
                                onDismiss()
                            }
                            .clip(MaterialTheme.shapes.small)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.TwoTone.CheckCircle,
                            null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Mark as completed")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            //Open edit modal here
                            onEditClick(selectedTask)
                            onDismiss()
                        }
                        .clip(MaterialTheme.shapes.small)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Icon(
                        Icons.TwoTone.Edit,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Edit task")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            taskViewModel.onTaskAction(
                                TaskActions.DeleteTask,
                                selectedTask
                            )
                            onDismiss()
                        }
                        .clip(MaterialTheme.shapes.small)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Icon(
                        Icons.TwoTone.Delete,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Delete task")
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

}