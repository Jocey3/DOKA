package com.doka

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {

    private val _sharedFlow =
        MutableSharedFlow<NavTarget>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun navigateTo(navTarget: NavTarget) {
        _sharedFlow.tryEmit(navTarget)
    }
}

enum class NavTarget(val label: String) {
    Splash("splash"),
    ImageSource("imageSource"),
    Edit("edit"),
    Exposure("exposure"),
    TimerExposure("timerExposure"),
    Settings("settings"),
    ExposureTimerSettings("exposureTimerSettings")
}