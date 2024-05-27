package com.doka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doka.ui.screens.edit.EditScreen
import com.doka.ui.screens.exposure.ExposureScreen
import com.doka.ui.screens.source_picture.ImageSourceScreen
import com.doka.ui.screens.splash.SplashScreen
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

    val vm: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavTarget.Splash.label
    ) {
        composable(NavTarget.Splash.label) {
            SplashScreen(
                navigateNext = { navigator.navigateTo(NavTarget.ImageSource) },
                viewModel = vm
            )
        }
        composable(NavTarget.ImageSource.label) {
            ImageSourceScreen(
                navigateNext = { navigator.navigateTo(NavTarget.Edit) },
                navigateBack = { navController.popBackStack() },
                viewModel = vm
            )
        }
        composable(NavTarget.Edit.label) {
            EditScreen(
                navigateNext = { navigator.navigateTo(NavTarget.Exposure) },
                navigateBack = { navController.popBackStack() }, viewModel = vm
            )
        }
        composable(NavTarget.Exposure.label) {
            ExposureScreen(
                navigateExpose = { },
                navigateSettings = { },
                navigateBack = { navController.popBackStack() }, viewModel = vm
            )
        }

    }
}