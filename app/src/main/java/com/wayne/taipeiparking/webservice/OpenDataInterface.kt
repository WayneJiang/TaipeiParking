package com.wayne.taipeiparking.webservice

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface OpenDataInterface {
    @Streaming
    @GET
    fun downloadFileAsync(@Url url: String): Deferred<ResponseBody>
}