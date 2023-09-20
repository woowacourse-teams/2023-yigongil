package com.created.team201.data.di

import android.util.Log
import com.created.domain.repository.AuthRepository
import com.created.team201.BuildConfig.TEAM201_BASE_URL
import com.created.team201.data.di.qualifier.AuthRetrofit
import com.created.team201.data.di.qualifier.DefaultRetrofit
import com.created.team201.data.remote.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONTENT_TYPE = "application/json"
    private const val CONNECT_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L
    private const val TAG_HTTP_LOG = "Http_Log"
    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d(TAG_HTTP_LOG, message)
        }.apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Singleton
    @Provides
    fun provideAuthInterceptor(authRepository: AuthRepository): AuthInterceptor =
        AuthInterceptor(authRepository)

    @Singleton
    @Provides
    @DefaultRetrofit
    fun provideDefaultRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(TEAM201_BASE_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()

    @Singleton
    @Provides
    @AuthRetrofit
    fun provideAuthRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(TEAM201_BASE_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
            addInterceptor(authInterceptor)
        }.build()
}