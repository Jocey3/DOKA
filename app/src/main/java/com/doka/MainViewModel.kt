package com.doka

import android.graphics.Bitmap
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var currentBitmap: Bitmap? = null
    var changedBitmap: Bitmap? = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val timeForExposure = mutableStateOf(0f)
    val exposure = mutableFloatStateOf(0f)
    val saturation = mutableFloatStateOf(1f)
    val contrast = mutableFloatStateOf(0f)
    val tint = mutableFloatStateOf(0f)
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)