package com.personal.onmyplate.ui.screens.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.onmyplate.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor() : ViewModel() {
    private val intentChannel: Channel<AddTaskAction> = Channel(Channel.UNLIMITED)
    val navigationChannel: Channel<AddTaskNavigation> = Channel(Channel.UNLIMITED)
    val initialState = AddTaskViewState()
    private val _state = initialState
    val state: Flow<AddTaskViewState> = compose()

    var task = Task()

    @OptIn(FlowPreview::class)
    private fun compose(): Flow<AddTaskViewState> =
        intentChannel
            .consumeAsFlow()
            .flatMapConcat {
                processAction(it)
            }
            .scan(_state) { state, result ->
                reducer(state, result)
            }

    private fun processAction(action: AddTaskAction): Flow<AddTaskResult> {
        return when (action) {
            is AddTaskAction.TaskNameUpdated -> nameUpdated(action.name)
            is AddTaskAction.TaskDescriptionUpdated -> descriptionUpdated(action.description)
            is AddTaskAction.IsDueAsapUpdated -> dueAsapCheckChanged(action.isDueAsap)
            is AddTaskAction.DueDateClicked -> flow {
                navigationChannel.send(AddTaskNavigation.DisplayDatePicker)
                emit(AddTaskResult.DateClicked)
            }
            is AddTaskAction.DueDateUpdated -> dueDateUpdated(action.dateInMillis)
        }
    }

    private fun nameUpdated(name: String): Flow<AddTaskResult> = flow {
        task.name = name
        emit(AddTaskResult.TaskNameUpdated(name))
    }

    private fun descriptionUpdated(description: String): Flow<AddTaskResult> = flow {
        task.description = description
        emit(AddTaskResult.TaskDescriptionUpdated(description))
    }

    private fun dueAsapCheckChanged(isChecked: Boolean): Flow<AddTaskResult> = flow {
        task.isDueAsap = isChecked
        emit(AddTaskResult.IsDueAsapUpdated(isChecked))
    }

    private fun dueDateUpdated(dateInMillis: Long): Flow<AddTaskResult> = flow {
        val formatter = DateTimeFormatter.ofPattern("EEE dd MMMM hh:mm a", Locale.ENGLISH)
        val date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(dateInMillis), ZoneId.systemDefault())
        val dateText = formatter.format(date)
        task.dueDate = date
        emit(AddTaskResult.DueDateUpdated(dateText))
    }

    private fun reducer(state: AddTaskViewState, result: AddTaskResult): AddTaskViewState {
        when (result) {
            is AddTaskResult.TaskNameUpdated -> state.apply {
                this.taskName = result.name
            }
            is AddTaskResult.TaskDescriptionUpdated -> state.apply {
                this.taskDescription = result.description
            }
            is AddTaskResult.IsDueAsapUpdated -> state.apply {
                this.isDueAsap = result.isDueAsap
            }
            is AddTaskResult.DateClicked -> {}
            is AddTaskResult.DueDateUpdated -> state.apply {
                this.dueDateText = result.dateText
            }
        }
        return _state
    }

    fun onAction(action: AddTaskAction) {
        viewModelScope.launch {
            intentChannel.send(action)
        }
    }

    fun resetNavigation() {
        viewModelScope.launch {
            navigationChannel.send(AddTaskNavigation.DoNothing)
        }
    }
}