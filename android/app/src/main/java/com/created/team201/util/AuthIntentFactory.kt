package com.created.team201.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver

object AuthIntentFactory {

    private const val KEY = "KEY"

    fun gitHubLogin(
        context: Context,
        uri: Uri,
        resultReceiver: ResultReceiver,
    ): Intent =
        Intent(context, CustomTabLauncherActivity::class.java)
            .putExtra(
                KEY,
                Bundle().apply {
                    putParcelable(GIT_CODE_KEY, resultReceiver)
                },
            )
}

const val GIT_CLIENT_KEY = "GIT_CLIENT_KEY"
const val GIT_CODE_KEY = "GIT_CODE_KEY"
const val CUSTOM_TABS_OPENED = "CUSTOM_TABS_OPENED"
