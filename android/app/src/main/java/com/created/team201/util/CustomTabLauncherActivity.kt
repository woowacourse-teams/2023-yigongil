package com.created.team201.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import androidx.appcompat.app.AppCompatActivity

class CustomTabLauncherActivity : AppCompatActivity() {

    private lateinit var fullUri: Uri
    private lateinit var resultReceiver: ResultReceiver
    private var customTabsOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData(intent = intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CUSTOM_TABS_OPENED, customTabsOpened)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        customTabsOpened =
            savedInstanceState.getBoolean(CUSTOM_TABS_OPENED, customTabsOpened)
    }

    private fun loadData(intent: Intent) {
        runCatching {
            resultReceiver =
                intent.extras?.getParcelableCompat<ResultReceiver>(GIT_CODE_KEY) as ResultReceiver
            fullUri =
                Uri.Builder().scheme("https").authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", intent.data.toString())
                    .build()
        }
    }

    override fun onResume() {
        super.onResume()

        if (!customTabsOpened) {
            customTabsOpened = true

            if (this::fullUri.isInitialized) {
                GitHubCustomTabsClient.open(this, fullUri)
                return
            }
            return
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        intent?.data?.let { sendOK(it) }
    }

    private fun sendOK(uri: Uri) {
        if (this::resultReceiver.isInitialized) {
            resultReceiver.send(
                Activity.RESULT_OK,
                Bundle().apply { putParcelable(GIT_CODE_KEY, uri) },
            )
        }
        finish()
    }

    private inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            getParcelable(key) as? T
        }
    }

    companion object {
        private const val GIT_CLIENT_KEY = "GIT_CLIENT_KEY"
        private const val GIT_CODE_KEY = "GIT_CODE_KEY"
        private const val CUSTOM_TABS_OPENED = "CUSTOM_TABS_OPENED"
    }
}
