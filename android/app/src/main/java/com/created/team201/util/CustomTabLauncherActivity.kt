package com.created.team201.util

import android.app.Activity
import android.content.Context
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
        intent.getBundleExtra(BUNDLE_KEY)?.apply {
            resultReceiver = getParcelableCompat<ResultReceiver>(RECEIVER_KEY) as ResultReceiver
            fullUri = Uri.parse(getString(GIT_URL_KEY))
        }
    }

    override fun onResume() {
        super.onResume()

        if (!customTabsOpened) {
            customTabsOpened = true

            if (::fullUri.isInitialized) GitHubCustomTabsClient.open(this, fullUri)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        intent?.data?.let { sendOK(it) }
    }

    private fun sendOK(uri: Uri) {
        if (::resultReceiver.isInitialized) {
            resultReceiver.send(
                Activity.RESULT_OK,
                Bundle().apply { putParcelable(GIT_OAUTH_TOKEN_KEY, uri) },
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
        internal const val GIT_OAUTH_TOKEN_KEY = "GIT_OAUTH_TOKEN_KEY"

        private const val CUSTOM_TABS_OPENED = "CUSTOM_TABS_OPENED"
        private const val BUNDLE_KEY = "BUNDLE_KEY"
        private const val GIT_URL_KEY = "GIT_URL"
        private const val RECEIVER_KEY = "RECEIVER_KEY"

        fun getIntent(
            context: Context,
            url: String,
            resultReceiver: ResultReceiver,
        ): Intent = Intent(context, CustomTabLauncherActivity::class.java).apply {
            putExtra(
                BUNDLE_KEY,
                Bundle().apply {
                    putParcelable(RECEIVER_KEY, resultReceiver)
                    putString(GIT_URL_KEY, url)
                },
            )
        }
    }
}
