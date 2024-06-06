package com.doka.domain.usecase

import android.graphics.Bitmap
import com.doka.domain.repository.Repository
import com.doka.util.Resource
import javax.inject.Inject

class GetPictureUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): Resource<Bitmap?> {
        return repository.getPicture()
    }
}