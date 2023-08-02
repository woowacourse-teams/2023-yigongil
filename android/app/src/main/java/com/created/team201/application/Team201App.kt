package com.created.team201.application

import android.app.Application
import android.content.Context
import com.created.team201.data.datasource.local.TokenStorage

class Team201App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: Team201App? = null

        private fun context(): Context =
            instance?.applicationContext ?: throw IllegalArgumentException()

        fun provideTokenStorage(): TokenStorage =
            TokenStorage.getInstance(context())
    }
}
