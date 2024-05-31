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
import com.doka.ui.screens.settings.SettingsScreen
import com.doka.ui.screens.settings.contrast.ContrastScreen
import com.doka.ui.screens.settings.exposure_e.ExposureEScreen
import com.doka.ui.screens.settings.exposure_timer.ExposureTimerScreen
import com.doka.ui.screens.settings.saturation.SaturationScreen
import com.doka.ui.screens.settings.tint.TintScreen
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
                navigateExpose = { },
                navigateSettings = { navigator.navigateTo(NavTarget.Settings) },
                navigateBack = { navController.popBackStack() }, sharedVM = sharedVM
            )
        }
        composable(NavTarget.Settings.label) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() },
                navigateExpTimer = { navigator.navigateTo(NavTarget.ExposureTimer) },
                navigateExposure = { navigator.navigateTo(NavTarget.ExposureE) },
                navigateSaturation = { navigator.navigateTo(NavTarget.Saturation) },
                navigateContrast = { navigator.navigateTo(NavTarget.Contrast) },
                navigateTint = { navigator.navigateTo(NavTarget.Tint) },
                )
        }
        composable(NavTarget.ExposureTimer.label) {
            ExposureTimerScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.ExposureE.label) {
            ExposureEScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Saturation.label) {
            SaturationScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Contrast.label) {
            ContrastScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
        composable(NavTarget.Tint.label) {
            TintScreen(
                navigateNext = { navController.popBackStack() },
                navigateBack = { navController.popBackStack() },
                sharedVM = sharedVM
            )
        }
    }
}