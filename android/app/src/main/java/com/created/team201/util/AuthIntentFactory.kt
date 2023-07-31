package com.created.team201.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver

object AuthIntentFactory {
    const val CUSTOM_TABS_OPENED = "CUSTOM_TABS_OPENED"
    const val BUNDLE_KEY = "BUNDLE_KEY"
    const val GIT_URL_KEY = "GIT_URL"
    const val RECEIVER_KEY = "RECEIVER_KEY"
    const val GIT_OAUTH_TOKEN_KEY = "GIT_OAUTH_TOKEN_KEY"

    fun gitHubLogin(
        context: Context,
        url: String,
        resultReceiver: ResultReceiver,
    ): Intent =
        Intent(context, CustomTabLauncherActivity::class.java)
            .putExtra(
                BUNDLE_KEY,
                Bundle().apply {
                    putParcelable(RECEIVER_KEY, resultReceiver)
                    putString(GIT_URL_KEY, url)
                },
            )
}
