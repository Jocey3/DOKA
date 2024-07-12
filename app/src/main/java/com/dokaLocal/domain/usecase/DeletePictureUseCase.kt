package com.dokaLocal.domain.usecase

import com.dokaLocal.domain.repository.Repository
import javax.inject.Inject


class DeletePictureUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        return repository.deletePicture()
    }
}