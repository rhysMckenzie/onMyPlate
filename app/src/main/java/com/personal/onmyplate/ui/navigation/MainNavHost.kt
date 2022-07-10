package com.personal.onmyplate.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.personal.onmyplate.ui.screens.addTask.AddTaskScreen
import com.personal.onmyplate.ui.screens.main.MainScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = "mainScreen") {
        composable(route = "mainScreen") { MainScreen(navController) }
        composable(
            route = "newTaskScreen",
            enterTransition = {
                when (initialState.destination.route) {
                    "mainScreen" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "mainScreen" ->
                        fadeOut(animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "mainScreen" ->
                        fadeIn(animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "mainScreen" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700))
                    else -> null
                }
            }) { AddTaskScreen(navController) }
    }
}