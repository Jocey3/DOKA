package com.doka.ui.screens.settings.contrast

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContrastViewModel @Inject constructor() : ViewModel() {

    val contrast = mutableFloatStateOf(1f)

    fun changeContrast(bitmap: Bitmap, contrast: Float): Bitmap {
        val adjustedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val cm = ColorMatrix()
        cm.set(floatArrayOf(
            contrast, 0f, 0f, 0f, 0f,
            0f, contrast, 0f, 0f, 0f,
            0f, 0f, contrast, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
        val cf = ColorMatrixColorFilter(cm)
        val paint = android.graphics.Paint()
        paint.colorFilter = cf
        val canvas = android.graphics.Canvas(adjustedBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return adjustedBitmap
    }
}