package com.doka.ui.screens.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor() : ViewModel() {
    private val _zoom = mutableStateOf(1f)
    val zoom: State<Float> = _zoom

    private val _angle = mutableStateOf(0f)
    val angle: State<Float> = _angle

    val _offset = mutableStateOf(Offset.Zero)
    val offset: State<Offset> = _offset

    var boxWidth = mutableFloatStateOf(330f)
    val boxHeight = mutableFloatStateOf(220f)
    var imageWidth = mutableFloatStateOf(179f)
    var imageHeight = mutableFloatStateOf(127f)

    fun updateZoom(newZoom: Float) {
        _zoom.value = newZoom
    }

    fun updateAngle(newAngle: Float) {
        _angle.value = newAngle
    }

    fun updateOffset(newOffset: Offset) {
        _offset.value = Offset(
            x = newOffset.x.coerceIn(0f, boxWidth.floatValue - imageWidth.floatValue),
            y = newOffset.y.coerceIn(0f, boxHeight.floatValue - imageHeight.floatValue)
        )
    }
}