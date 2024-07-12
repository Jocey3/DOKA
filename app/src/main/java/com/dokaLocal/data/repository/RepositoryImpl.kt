package com.dokaLocal.data.repository

import android.graphics.Bitmap
import com.dokaLocal.data.data_source.network.RemoteDataSource
import com.dokaLocal.domain.repository.Repository
import com.dokaLocal.util.Resource
import com.dokaLocal.util.adjustedImage
import com.dokaLocal.util.getBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getPicture(): Resource<Bitmap?> {
        return withContext(Dispatchers.IO) {
            val response = remoteDataSource.getPicture()
            if (response.isSuccessful) {
                val bitmap = response.body()?.getBitmap()?.adjustedImage()
                if (bitmap != null) {
                    Resource.Success(bitmap)
                } else {
                    Resource.Error("Failed to decode bitmap")
                }
            } else {
                Resource.Error("Please upload some image")
            }
        }
    }

    override suspend fun deletePicture() {
        return withContext(Dispatchers.IO) {
            remoteDataSource.deletePicture()
        }
    }
}
