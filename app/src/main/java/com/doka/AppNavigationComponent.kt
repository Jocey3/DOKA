package com.doka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doka.ui.screens.done.DoneScreen
import com.doka.ui.screens.edit.EditScreen
import com.doka.ui.screens.exposure.ExposureScreen
import com.doka.ui.screens.settings.SettingsScreen
import com.doka.ui.screens.settings.exposure_timer.ExposureTimerSettingsScreen
import com.doka.ui.screens.source_picture.ImageSourceScreen
import com.doka.ui.screens.splash.SplashScreen
import com.doka.ui.screens.timer_default.TimerDefaultScreen
import com.doka.ui.screens.timer_exposure.TimerExposureScreen
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

    val sharedVM: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavTarget.Splash.label
    ) {
        composable(NavTarget.Splash.label) {
            SplashScreen(
                navigateNext = { navigator.navigateTo(NavTarget.ImageSource) },
                viewModel = sharedVM
            )
        }
        composable(NavTarget.ImageSource.label) {
            ImageSourceScreen(
                navigateNext = { navigator.navigateTo(NavTarget.Edit) },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Edit.label) {
            EditScreen(
                navigateNext = { navigator.navigateTo(NavTarget.Exposure) },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Exposure.label) {
            ExposureScreen(
                navigateExpose = { navigator.navigateTo(NavTarget.TimerExposure) },
                navigateSettings = { navigator.navigateTo(NavTarget.Settings) },
                navigateBack = { navController.popBackStack() }, sharedVM = sharedVM
            )
        }
        composable(
            NavTarget.TimerExposure.label
        ) {
            TimerExposureScreen(
                navigateNext = {
                    sharedVM.defaultScreen.value += 1
                    navigator.navigateTo(NavTarget.TimerDefault)
                },
                navigateBack = { navController.popBackStack(NavTarget.Exposure.label, false) },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.TimerDefault.label) {
            TimerDefaultScreen(
                navigateNext = {
                    if (sharedVM.defaultScreen.value < 2) {
                        sharedVM.defaultScreen.value += 1
                        navigator.navigateTo(NavTarget.TimerDefault)
                    } else {
                        navigator.navigateTo(NavTarget.Done)
                    }
                },
                navigateBack = { navController.popBackStack(NavTarget.Exposure.label, false) },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Done.label) {
            DoneScreen(
                navigateNext = { navController.popBackStack(NavTarget.Splash.label, false) },
                navigateBack = { navController.popBackStack(NavTarget.Exposure.label, false) },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Settings.label) {
            SettingsScreen(
                navigateExpTimer = { navigator.navigateTo(NavTarget.ExposureTimerSettings) },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(NavTarget.ExposureTimerSettings.label) {
            ExposureTimerSettingsScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }

    }
}