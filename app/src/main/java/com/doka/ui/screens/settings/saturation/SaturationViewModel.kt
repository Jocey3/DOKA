package com.doka.ui.screens.settings.saturation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class SaturationViewModel @Inject constructor() : ViewModel() {

    val saturation = mutableFloatStateOf(1f)

    fun changeBitmapSaturationOptimized(bitmap: Bitmap, saturation: Float): Bitmap {
        val scale = 0.1f
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        val newBitmap = Bitmap.createBitmap(scaledBitmap.width, scaledBitmap.height, scaledBitmap.config)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        val canvas = Canvas(newBitmap)
        canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)
        return Bitmap.createScaledBitmap(newBitmap, bitmap.width, bitmap.height, true)
    }

    fun changeBitmapSaturationOld(bitmap: Bitmap, saturation: Float): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(newBitmap)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }
}