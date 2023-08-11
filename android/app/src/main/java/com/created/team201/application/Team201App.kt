package com.created.team201.application

import android.app.Application
import android.content.Context
import com.created.team201.data.datasource.local.OnBoardingIsDoneStorage
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.local.TokenStorage
import com.created.team201.data.datasource.remote.login.AuthDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.remote.interceptor.AuthInterceptor
import com.created.team201.data.repository.AuthRepositoryImpl

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
        OnBoardingIsDoneStorage.getInstance(this)
    }

    companion object {
        private var instance: Team201App? = null

        private fun context(): Context =
            instance?.applicationContext ?: throw IllegalArgumentException()

        fun provideTokenStorage(): TokenStorage =
            TokenStorage.getInstance(context())

        fun provideAuthInterceptor(): AuthInterceptor =
            AuthInterceptor(
                AuthRepositoryImpl(
                    authDataSource = AuthDataSourceImpl(NetworkServiceModule.authService),
                    tokenDataSource = TokenDataSourceImpl(provideTokenStorage()),
                ),
            )

        fun provideOnBoardingIsDoneStorage(): OnBoardingIsDoneStorage =
            OnBoardingIsDoneStorage.getInstance(context())
    }
}
