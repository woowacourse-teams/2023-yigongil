package com.created.team201.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenStorage(context: Context) {

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

    fun putToken(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun fetchToken(key: String): String? {
        return storage.getString(key, null)
    }

    fun isGuest(key: String): Boolean {
        return storage.getString(key, null).isNullOrEmpty()
    }

    companion object {
        private const val FILE_NAME = "TEAM201_STORAGE"

        @Volatile
        private var instance: TokenStorage? = null

        fun getInstance(context: Context): TokenStorage {
            synchronized(this) {
                instance?.let { return it }
                return TokenStorage(context).also { instance = it }
            }
        }
    }
}
