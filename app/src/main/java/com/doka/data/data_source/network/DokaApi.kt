package com.doka.data.data_source.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface DokaApi {

    @GET("pictures")
    suspend fun getPicture(): Response<ResponseBody>

    @DELETE("delete")
    suspend fun deletePicture(): Response<ResponseBody>
}