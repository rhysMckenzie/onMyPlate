package com.personal.onmyplate.ui.screens.addTask

sealed class AddTaskAction {
    data class TaskNameUpdated(val name: String) : AddTaskAction()
    data class TaskDescriptionUpdated(val description: String) : AddTaskAction()
    data class IsDueAsapUpdated(val isDueAsap: Boolean) : AddTaskAction()
    object DueDateClicked : AddTaskAction()
    data class DueDateUpdated(val dateInMillis: Long) : AddTaskAction()
}