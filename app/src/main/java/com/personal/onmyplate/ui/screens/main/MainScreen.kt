package com.personal.onmyplate.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.personal.onmyplate.ui.theme.AppTheme
import com.personal.onmyplate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
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
                                text = stringResource(id = R.string.main_list_heading),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                                  navController.navigate("newTaskScreen")
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            ) {

            }
        }
    }
}