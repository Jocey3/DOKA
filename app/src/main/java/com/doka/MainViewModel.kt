package com.doka

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _zoom = mutableStateOf(1f)
    val zoom: State<Float> = _zoom

    private val _offset = mutableStateOf(Offset.Zero)
    val offset: State<Offset> = _offset

    private val _angle = mutableStateOf(0f)
    val angle: State<Float> = _angle


    var currentBitmap: Bitmap? = null

    var boxCoordinates = mutableStateOf(0 to 0)
    var boxSize = mutableStateOf(androidx.compose.ui.geometry.Size.Zero)
    var imageCoordinates = mutableStateOf(0 to 0)
    var imageSize = mutableStateOf(androidx.compose.ui.geometry.Size.Zero)

    fun updateZoom(newZoom: Float) {
        _zoom.value = newZoom
    }

    fun updateOffset(newOffset: Offset) {
        _offset.value = newOffset
    }

    fun updateAngle(newAngle: Float) {
        _angle.value = newAngle
    }
}