package org.example.mytask.presentation.screens.tasks

enum class TaskActions(val title: String) {
    AddTask("Add task"),
    EditTask("Edit task"),
    MarkAsCompleted("Mark as completed"),
    UnMarkAsCompleted("Unmark as completed"),
    MarkAsFavorite("Mark as favorite"),
    UnmarkAsFavorite("Unmark as favorite"),
    DeleteTask("Delete task");
}