package com.created.team201.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object MockNetworkModule {
    private const val CONTENT_TYPE = "application/json"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MockServer.server.url(""))
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()

    inline fun <reified T> create(): T = retrofit.create<T>(T::class.java)
}
