package com.doka.ui.screens.source_picture

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doka.domain.usecase.GetPictureUseCase
import com.doka.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSourceViewModel @Inject constructor(
    private val getPictureUseCase: GetPictureUseCase
) : ViewModel() {
    var state by mutableStateOf(ImageSourceScreenState())
        private set

    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        getPicture()
    }

    private fun getPicture() {
        viewModelScope.launch(handlerException) {
            when (val picture = getPictureUseCase()) {
                is Resource.Success -> {
                    state = ImageSourceScreenState(picture = picture.data, isLoading = false)
                }

                is Resource.Error -> {
                    state = state.copy(message = picture.message)
                    delay(5000)
                    getPicture()
                }
            }
        }
    }

}
