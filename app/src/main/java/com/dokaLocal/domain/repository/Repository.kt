package com.dokaLocal.domain.repository

import android.graphics.Bitmap
import com.dokaLocal.util.Resource

interface Repository {
    suspend fun getPicture(): Resource<Bitmap?>

    suspend fun deletePicture()
}