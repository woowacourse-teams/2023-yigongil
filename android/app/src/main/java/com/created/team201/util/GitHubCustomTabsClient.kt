package com.created.team201.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

object GitHubCustomTabsClient {

    fun open(context: Context, uri: Uri) {
        CustomTabsIntent.Builder().setUrlBarHidingEnabled(true).setShowTitle(true).build()
            .launchUrl(context, uri)
    }
}
