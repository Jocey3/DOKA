package com.doka.ui.screens.timer_exposure

import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerExposureViewModel @Inject constructor() : ViewModel() {
    val maxTime = mutableStateOf(60)
    val timeLeft = mutableStateOf(maxTime.value)
    val timeSpent = mutableStateOf(0)
    val progress = mutableStateOf(1f)
    val paused = mutableStateOf(false)
    var navigateNext: () -> Unit = {}
    var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null
    fun loadProgress() {
        timerJob?.cancel() // Cancel the previous timer job if any
        timerJob = viewModelScope.launch {
            for (i in maxTime.value - timeSpent.value downTo 0) {
                if (paused.value) {
                    return@launch
                }
                timeLeft.value = i
                for (j in 9 downTo 0) {
                    if (paused.value) {
                        return@launch
                    }
                    val fractionalProgress = i.toFloat() + (j.toFloat() / 10)
                    progress.value = fractionalProgress / maxTime.value
                    delay(100L)
                }
                timeSpent.value = ++timeSpent.value
            }
            playBeeps()
        }
    }

    private fun playBeeps() {
        viewModelScope.launch {
            repeat(3) {
                mediaPlayer?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    seekTo(0)
                    start()
                }
                delay(1000)
            }
            navigateNext()
        }
    }

    fun pauseTimer() {
        paused.value = true
    }

    fun resumeTimer() {
        paused.value = false
        loadProgress()
    }

    override fun onCleared() {
        timerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }

}