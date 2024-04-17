package com.jd.localsend;

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


val service: Network by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.59:5000/")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    retrofit.create(Network::class.java)
}

interface Network {
    @POST("book/{book_name}")
    suspend fun book(@Path("book_name") bookName: String, @Body data: RequestBody)

    @GET("book/{book_name}")
    suspend fun bookExists(@Path("book_name") bookName: String): Boolean
}
