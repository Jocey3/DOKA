package com.dokaLocal.ui.screens.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokaLocal.domain.usecase.DeletePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class EditViewModel @Inject constructor(
    private val deletePictureUseCase: DeletePictureUseCase
) : ViewModel() {
    private val _frameSize: MutableState<Size?> = mutableStateOf(null)
    val frameSize: State<Size?> = _frameSize

    private val _imageFrameSize: MutableState<Size?> = mutableStateOf(null)
    val imageFrameSize: State<Size?> = _imageFrameSize

    private val _realImageSize: MutableState<Size?> = mutableStateOf(null)
    val realImageSize: State<Size?> = _realImageSize

    private val _offset: MutableState<Offset?> = mutableStateOf(null)
    val offset: State<Offset?> = _offset

    private val _zoom = mutableFloatStateOf(1f)
    val zoom: State<Float> = _zoom

    private val _angle = mutableFloatStateOf(0f)
    val angle: State<Float> = _angle

    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        deletePicture()

    }

    fun setFrameSize(width: Float, height: Float) {
        _frameSize.value = Size(width, height)
    }

    fun setImageFrameSize(width: Float, height: Float, widthDp: Dp, heightDp: Dp) {
        _imageFrameSize.value = Size(width, height, widthDp, heightDp)
    }

    fun setRealImageSize(bitmapWidth: Int, bitmapHeight: Int, density: Density) {
        imageFrameSize.value?.let {
            val scale = min(it.width / bitmapWidth, it.height / bitmapHeight)
            val scaledWidth = bitmapWidth * scale
            val scaledHeight = bitmapHeight * scale

            val widthDp = with(density) { scaledWidth.toDp() }
            val heightDp = with(density) { scaledHeight.toDp() }

            _realImageSize.value = Size(scaledWidth, scaledHeight, widthDp, heightDp)
        }
    }

    fun updateOffset(newOffset: Offset) {
        frameSize.value?.let { frameSizeValue ->
            realImageSize.value?.let { realImageSize ->
                _offset.value = Offset(
                    x = newOffset.x.coerceIn(0f, frameSizeValue.width - realImageSize.width),
                    y = newOffset.y.coerceIn(0f, frameSizeValue.height - realImageSize.height)
                )
            }
        }
    }

    fun updateZoom(newZoom: Float) {
        _zoom.floatValue = newZoom
    }

    fun updateAngle() {
        if (angle.value < 360f) _angle.floatValue += 90f
        else _angle.floatValue = 90f
    }

    private fun deletePicture() {
        viewModelScope.launch(handlerException) {
            deletePictureUseCase()
        }
    }

}

data class Size(
    val width: Float,
    val height: Float,
    val widthDp: Dp? = null,
    val heightDp: Dp? = null
)
