package com.dokaLocal.domain.usecase

import android.graphics.Bitmap
import com.dokaLocal.domain.repository.Repository
import com.dokaLocal.util.Resource
import javax.inject.Inject

class GetPictureUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): Resource<Bitmap?> {
        return repository.getPicture()
    }
}