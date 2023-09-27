package com.created.team201.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class OnBoardingStorage @Inject constructor(context: Context) {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val storage: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    private val editor: SharedPreferences.Editor = storage.edit()

    fun putIsDone(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun fetchIsDone(key: String): Boolean {
        return storage.getBoolean(key, false)
    }

    companion object {
        private const val FILE_NAME = "TEAM201_STORAGE_ONBOARDING"
    }
}
