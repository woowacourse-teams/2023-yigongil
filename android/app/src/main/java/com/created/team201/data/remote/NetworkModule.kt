package com.created.team201.data.remote

import com.created.team201.BuildConfig.TEAM201_BASE_URL
import com.created.team201.data.remote.api.HomeService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
object NetworkModule {
    private const val CONTENT_TYPE = "application/json"

    @ExperimentalSerializationApi
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TEAM201_BASE_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()

    val homeService: HomeService by lazy { retrofit.create(HomeService::class.java) }
}
