package org.example.mytask.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.mytask.domain.models.Task

@Composable
fun TaskCard(
    task: Task,
    onCardClick: (Task) -> Unit = {},
    onMoreClick: (Task) -> Unit = {},
    onFavoriteClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickable { onCardClick(task) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                if (task.isCompleted) MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.6f
                ) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0f)
            )
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .border(
                        4.dp,
                        if (task.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            ) {
                Text(
                    task.title, maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    task.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall

                )
            }
            Column {
                IconButton(onClick = { onMoreClick(task) }) {
                    Icon(
                        imageVector = Icons.TwoTone.MoreVert,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "More"
                    )
                }
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
        }
    }
}
