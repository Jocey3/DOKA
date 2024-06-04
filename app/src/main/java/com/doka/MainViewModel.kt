package com.doka

import android.graphics.Bitmap
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var currentBitmap: Bitmap? = null
    var changedBitmap: Bitmap?  = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val exposure = mutableFloatStateOf(1f)
    val saturation = mutableFloatStateOf(1f)
    val contrast = mutableFloatStateOf(1f)
    val tint = mutableFloatStateOf(0f)
    val timeForExposure = mutableStateOf(30f)
    val defaultScreen = mutableStateOf(0)

    fun clearData() {
        setSettingsDefault()
        currentBitmap = null
        defaultScreen.value = 0
        savedImagesSettings.value = ImageSettings(0f, 0f, 0f, 0f)
    }

    fun setSettingsDefault(){
        saturation.floatValue = 1f
        exposure.floatValue = 1f
        contrast.floatValue = 1f
        tint.floatValue = 0f
    }

    private fun scaleBitmapTo(
        destinationHeight: Int,
        destinationWidth: Int,
        bitmap: Bitmap?
    ): Bitmap {
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap!!, destinationWidth, destinationHeight, false)
        val outStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
        return scaledBitmap
    }

    fun loadCompressedBitmap(bitmap: Bitmap): Bitmap {
        val scaledBitmap: Bitmap
        val origWidth = bitmap.width
        val origHeight = bitmap.height
        val destHeight: Int
        val destWidth: Int
        if (origWidth > 3000) {
            destWidth = origWidth / 5
            destHeight = origHeight / 5
            scaledBitmap = scaleBitmapTo(destHeight, destWidth, bitmap)
        } else if (origWidth > 1000) {
            destHeight = origHeight / 2
            destWidth = origWidth / 2
            scaledBitmap = scaleBitmapTo(destHeight, destWidth, bitmap)
        } else scaledBitmap = bitmap
        return scaledBitmap
    }
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)