package com.created.team201.util

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

object FirebaseLogUtil {
    const val SCREEN_HOME = "view_home"
    const val SCREEN_STUDY_LIST = "view_study_list"
    const val SCREEN_STUDY_MANAGE = "view_study_manage"
    const val SCREEN_STUDY_MANAGE_PARTICIPATED = "view_study_manage_participated"
    const val SCREEN_STUDY_MANAGE_OPENED = "view_study_manage_opened"
    const val SCREEN_CHAT = "view_chat"
    const val SCREEN_MY_PAGE = "view_my_page"

    fun logScreenEvent(screenName: String, screenClass: String) {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }
}
