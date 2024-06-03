package com.doka.ui.screens.timer_exposure

import android.media.MediaPlayer
import android.util.Log
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
    var navigateNext: () -> Unit = {}
    var mediaPlayer: MediaPlayer? = null

    fun loadProgress() {
        viewModelScope.launch {
            Log.d("Mdhfsfj", "max ${maxTime.value}")

            for (i in maxTime.value downTo 0) {
                if (paused.value) {
                    return@launch
                }
                timeLeft.value = i
                progress.value = i.toFloat() / maxTime.value
                delay(1000)
            }
            playBeepSound()
            navigateNext()
        }
    }

    fun pauseTimer() {
        paused.value = true
        maxTime.value = timeLeft.value
    }

    fun resumeTimer() {
        paused.value = false
        loadProgress()
    }

    fun playBeepSound() {
        mediaPlayer?.start()
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }

}