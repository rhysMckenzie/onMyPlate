package com.personal.onmyplate.ui.screens.addTask

sealed class AddTaskResult {
    data class TaskNameUpdated(val name: String) : AddTaskResult()
    data class TaskDescriptionUpdated(val description: String) : AddTaskResult()
    data class IsDueAsapUpdated(val isDueAsap: Boolean) : AddTaskResult()
    data class DueDateUpdated(val dateText: String) : AddTaskResult()
    object DateClicked : AddTaskResult()
}