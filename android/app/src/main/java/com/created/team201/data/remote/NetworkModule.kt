package com.created.team201.data.remote

import android.util.Log
import com.created.team201.BuildConfig.TEAM201_BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
object NetworkModule {
    private const val CONTENT_TYPE = "application/json"

    private const val AUTHORIZATION = "Authorization"
    private const val JWT_USER_TOKEN = "JWT %s"
    private const val CONNECT_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L

    private const val TOKEN = "1"

    private const val TAG_HTTP_LOG = "Http_Log"

    private val client = OkHttpClient.Builder().apply {
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        addInterceptor(createTokenInterceptor(TOKEN))
        addInterceptor(createHttpLoggingInterceptor())
    }.build()

    @ExperimentalSerializationApi
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TEAM201_BASE_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .client(client)
        .build()

    private fun createTokenInterceptor(token: String): Interceptor = Interceptor { chain ->
        with(chain) {
            val modifiedRequest = request().newBuilder()
                .addHeader(AUTHORIZATION, token)
                .build()

            proceed(modifiedRequest)
        }
    }

    private fun createHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d(TAG_HTTP_LOG, message)
        }.apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    inline fun <reified T> create(): T = retrofit.create<T>(T::class.java)
}
