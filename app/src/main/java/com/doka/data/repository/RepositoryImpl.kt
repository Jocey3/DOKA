package com.doka.data.repository

import android.graphics.Bitmap
import com.doka.data.data_source.network.RemoteDataSource
import com.doka.domain.repository.Repository
import com.doka.util.Resource
import com.doka.util.getBitmap
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
                val bitmap = response.body()?.getBitmap()
                if (bitmap != null) {
                    Resource.Success(bitmap)
                } else {
                    Resource.Error("Failed to decode bitmap")
                }
            } else {
                Resource.Error("Unexpected response code: ${response.code()}")
            }
        }
    }
}
