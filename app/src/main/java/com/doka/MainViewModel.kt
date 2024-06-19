package com.doka

import android.graphics.Bitmap
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var beforeExposure: Bitmap? = null
    var currentBitmap: Bitmap? = null
    var changedBitmap: Bitmap? = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val exposure = mutableFloatStateOf(1f)
    val saturation = mutableFloatStateOf(1f)
    val contrast = mutableFloatStateOf(1f)
    val tint = mutableFloatStateOf(0f)
    val timeForExposure = mutableFloatStateOf(30f)

    fun clearData() {
        setSettingsDefault()
        beforeExposure = null
        currentBitmap = null
        savedImagesSettings.value = ImageSettings(0f, 0f, 0f, 0f)
    }

    private fun setSettingsDefault() {
        saturation.floatValue = 1f
        exposure.floatValue = 1f
        contrast.floatValue = 1f
        tint.floatValue = 0f
    }
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)