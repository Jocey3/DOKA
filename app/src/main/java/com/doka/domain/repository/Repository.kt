package com.doka.domain.repository

import android.graphics.Bitmap
import com.doka.util.Resource

interface Repository {
    suspend fun getPicture(): Resource<Bitmap?>

    suspend fun deletePicture()
}