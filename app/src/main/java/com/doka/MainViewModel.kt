package com.doka

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var currentBitmap: Bitmap? = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val timeForExposure = mutableStateOf(0f)
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)