package com.doka.data.data_source.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming

interface DokaApi {

    @GET("/picture")
    @Streaming
    suspend fun getPicture(): Response<ResponseBody>
}