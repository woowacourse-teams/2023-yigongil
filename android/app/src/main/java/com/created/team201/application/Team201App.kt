package com.created.team201.application

import android.app.Application
import android.content.Context
import com.created.team201.data.datasource.local.OnBoardingStorage
import com.created.team201.data.datasource.local.DefaultTokenDataSource
import com.created.team201.data.datasource.local.TokenStorage
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.remote.interceptor.AuthInterceptor
import com.created.team201.data.repository.DefaultAuthRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Team201App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        initTokenStorage()
        initOnBoardingIsDoneStorage()
    }

    private fun initTokenStorage() {
        TokenStorage.getInstance(this)
    }

    private fun initOnBoardingIsDoneStorage() {
        OnBoardingStorage.getInstance(this)
    }

    companion object {
        private var instance: Team201App? = null

        private fun context(): Context =
            instance?.applicationContext ?: throw IllegalArgumentException()

        fun provideTokenStorage(): TokenStorage =
            TokenStorage.getInstance(context())

        fun provideAuthInterceptor(): AuthInterceptor =
            AuthInterceptor(
                DefaultAuthRepository(
                    authService = NetworkServiceModule.authService,
                    tokenDataSource = DefaultTokenDataSource(provideTokenStorage()),
                ),
            )

        fun provideOnBoardingIsDoneStorage(): OnBoardingStorage =
            OnBoardingStorage.getInstance(context())
    }
}
