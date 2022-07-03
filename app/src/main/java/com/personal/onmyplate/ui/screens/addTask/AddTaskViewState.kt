package com.personal.onmyplate.ui.screens.addTask

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class AddTaskViewState {
    var taskName by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var isDueAsap by mutableStateOf(false)
    var dueDateText by mutableStateOf("")
}