package com.personal.onmyplate.ui.screens.addTask

sealed class AddTaskNavigation {
    object DisplayDatePicker : AddTaskNavigation()
    object DoNothing : AddTaskNavigation()
}