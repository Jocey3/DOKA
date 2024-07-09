package com.doka.data.data_source.network


import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: DokaApi) {

    suspend fun getPicture(): Response<ResponseBody> {
        return api.getPicture()
    }

    suspend fun deletePicture(): Response<ResponseBody> {
        return api.deletePicture()
    }

}