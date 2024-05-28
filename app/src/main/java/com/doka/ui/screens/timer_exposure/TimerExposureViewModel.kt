package com.doka.ui.screens.timer_exposure

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerExposureViewModel @Inject constructor() : ViewModel() {
    val maxTime = mutableStateOf(60)
    val timeLeft = mutableStateOf(maxTime.value)
    val progress = mutableStateOf(1f)
    val paused = mutableStateOf(false)

    init {
        viewModelScope.launch {
            loadProgress()
        }
    }

    private suspend fun loadProgress() {
        for (i in maxTime.value downTo 0) {
            if (paused.value) {
                return
            }
            timeLeft.value = i
            progress.value = i.toFloat() / maxTime.value
            delay(1000)
        }
    }

    fun pauseTimer() {
        paused.value = true
        maxTime.value = timeLeft.value
    }

    fun resumeTimer() {
        paused.value = false
        viewModelScope.launch {
            loadProgress()
        }
    }
}