package com.doka.domain.usecase

import com.doka.domain.repository.Repository
import javax.inject.Inject


class DeletePictureUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        return repository.deletePicture()
    }
}