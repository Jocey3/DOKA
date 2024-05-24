package com.doka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doka.ui.edit.EditScreen
import com.doka.ui.source_picture.ImageSourceScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
    navController: NavHostController = rememberNavController(),
    navigator: Navigator
) {

    LaunchedEffect("navigation") {
        navigator.sharedFlow.onEach {
            navController.navigate(it.label)
        }.launchIn(this)
    }

    NavHost(
        navController = navController,
        startDestination = NavTarget.ImageSource.label
    ) {
        composable(NavTarget.ImageSource.label) {
            ImageSourceScreen(navigateNext = { navigator.navigateTo(NavTarget.Edit) })
        }
        composable(NavTarget.Edit.label) {
            EditScreen()
        }
    }
}