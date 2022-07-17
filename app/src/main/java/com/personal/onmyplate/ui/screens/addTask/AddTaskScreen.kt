package com.personal.onmyplate.ui.screens.addTask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.personal.onmyplate.R
import com.personal.onmyplate.ui.theme.AppTheme
import com.personal.onmyplate.utils.getActivity
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTaskScreen(navController: NavController) {
    val viewModel: AddTaskViewModel = viewModel()
    val viewState by viewModel.state.collectAsState(initial = viewModel.initialState)
    val keyboardController = LocalSoftwareKeyboardController.current

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.create_new_task),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Close",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                }
            ) {
                NavigationComposable(viewModel)

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = viewState.taskName,
                        label = {
                            Text(text = stringResource(id = R.string.task_name))
                        },
                        onValueChange = { value ->
                            viewModel.onAction(AddTaskAction.TaskNameUpdated(value))
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = viewState.taskDescription,
                        label = {
                            Text(text = stringResource(id = R.string.task_description))
                        },
                        onValueChange = { value ->
                            viewModel.onAction(AddTaskAction.TaskDescriptionUpdated(value))
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = viewState.isDueAsap, onCheckedChange = { isChecked ->
                            viewModel.onAction(AddTaskAction.IsDueAsapUpdated(isChecked))
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.due_asap),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.onAction(AddTaskAction.DueDateClicked)
                    }) {
                        TextField(
                            value = viewState.dueDateText,
                            label = {
                                Text(text = stringResource(id = R.string.due_date))
                            },
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationComposable(viewModel: AddTaskViewModel) {
    val navigation by viewModel.navigationChannel.receiveAsFlow()
        .collectAsState(initial = AddTaskNavigation.DoNothing)

    when (navigation) {
        is AddTaskNavigation.DisplayDatePicker -> {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.show(
                LocalContext.current.getActivity()!!.supportFragmentManager,
                picker.toString()
            )
            picker.addOnPositiveButtonClickListener { dateMillis ->
                viewModel.onAction(AddTaskAction.DueDateUpdated(dateMillis))
                viewModel.resetNavigation()
            }
            picker.addOnCancelListener {
                viewModel.resetNavigation()
            }
            picker.addOnDismissListener {
                viewModel.resetNavigation()
            }
            picker.addOnNegativeButtonClickListener {
                viewModel.resetNavigation()
            }
        }
        AddTaskNavigation.DoNothing -> {

        }
    }
}