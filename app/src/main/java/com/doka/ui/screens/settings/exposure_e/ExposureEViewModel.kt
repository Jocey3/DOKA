package com.doka.ui.screens.settings.exposure_e

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExposureEViewModel @Inject constructor() : ViewModel() {

    val exposure = mutableFloatStateOf(1f)

    fun changeExposure(bitmap: Bitmap, exposure: Float): Bitmap {
        val adjustedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(adjustedBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setScale(exposure, exposure, exposure, 1f)
            })
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return adjustedBitmap
    }
}