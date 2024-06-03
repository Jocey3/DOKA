package com.doka

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var currentBitmap: Bitmap? = null
    var changedBitmap: Bitmap?  = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val exposure = mutableFloatStateOf(0f)
    val saturation = mutableFloatStateOf(1f)
    val contrast = mutableFloatStateOf(0f)
    val tint = mutableFloatStateOf(0f)
    val timeForExposure = mutableStateOf(30f)
    val defaultScreen = mutableStateOf(0)

    fun clearData() {
        currentBitmap = null
        defaultScreen.value = 0
        savedImagesSettings.value = ImageSettings(0f, 0f, 0f, 0f)
    }
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)