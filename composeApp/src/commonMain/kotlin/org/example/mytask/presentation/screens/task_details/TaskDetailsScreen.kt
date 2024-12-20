package org.example.mytask.presentation.screens.task_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.mytask.domain.models.Task

@Composable
fun TaskDetailsScreen(
    task: Task, onFavoriteClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp).verticalScroll(
                state = rememberScrollState(),
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = if (task.isCompleted) "✔" else "❌",
                color = if (task.isCompleted) Color.Green else Color.Red,
                style = if (task.isCompleted) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelSmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onFavoriteClick(task) }) {
                if (task.isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Favorite"
                    )
                } else {
                    Icon(
                        imageVector = Icons.TwoTone.FavoriteBorder,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Un favorite"
                    )
                }
            }
        }
        Text(
            text = "Description :",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}