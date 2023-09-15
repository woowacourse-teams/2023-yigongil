package com.created.team201.data.remote

import android.util.Log
import com.created.team201.BuildConfig.TEAM201_BASE_URL
import com.created.team201.application.Team201App
import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.api.StudyListService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val CONTENT_TYPE = "application/json"
    private const val CONNECT_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L
    private const val TAG_HTTP_LOG = "Http_Log"

    val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TEAM201_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .build()
    }

    private val client by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(createHttpLoggingInterceptor())
            addInterceptor(Team201App.provideAuthInterceptor())
        }.build()
    }

    private fun createHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d(TAG_HTTP_LOG, message)
        }.apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TEAM201_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .client(client)
            .build()
    }

    inline fun <reified T> create(): T = when (T::class) {
        AuthService::class -> authRetrofit.create(T::class.java)
        StudyListService::class -> authRetrofit.create(T::class.java)
        else -> retrofit.create(T::class.java)
    }
}
