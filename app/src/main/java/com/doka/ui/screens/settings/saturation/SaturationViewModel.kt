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
    private var currentJob: Job? = null
    var changedBitmap: Bitmap? = null
    var _originalBitmap: Bitmap? = null


    fun changeBitmapSaturationOptimized(bitmap: Bitmap, saturation: Float): Bitmap {
        val scale = 0.5f
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

    fun loadImage() {
        changedBitmap = _originalBitmap
    }

    fun changeSaturation(saturation: Float, partSize: Int = 20) {
        _originalBitmap.let { originalBitmap ->
            viewModelScope.launch {
                changedBitmap = processBitmapInParts(originalBitmap!!, saturation, partSize)
            }
        }
    }

    private suspend fun processBitmapInParts(bitmap: Bitmap, saturation: Float, partSize: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val width = bitmap.width
        val height = bitmap.height
        withContext(Dispatchers.Default) {
            val jobs = mutableListOf<Deferred<Bitmap>>()
            for (y in 0 until height step partSize) {
                for (x in 0 until width step partSize) {
                    val partWidth = min(partSize, width - x)
                    val partHeight = min(partSize, height - y)
                    jobs.add(async {
                        processBitmapPart(bitmap, x, y, partWidth, partHeight, saturation)
                    })
                }
            }
            jobs.awaitAll().forEachIndexed { index, partBitmap ->
                val x = (index % (width / partSize)) * partSize
                val y = (index / (width / partSize)) * partSize
                val canvas = Canvas(newBitmap)
                canvas.drawBitmap(partBitmap, x.toFloat(), y.toFloat(), null)
            }
        }
        return newBitmap
    }

    private fun processBitmapPart(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int, saturation: Float): Bitmap {
        val partBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)
        return changeBitmapSaturation(partBitmap, saturation)
    }

    private fun changeBitmapSaturation(bitmap: Bitmap, saturation: Float): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(newBitmap)
        val colorMatrix = ColorMatrix().apply { setSaturation(saturation) }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }
}