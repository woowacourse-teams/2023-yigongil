package com.created.team201.data.repository

import android.util.Log
import com.created.domain.model.AppUpdateInformation
import com.created.team201.BuildConfig
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SplashRepository {

    fun getAppUpdateInformation(
        versionCode: Int,
        onAppUpdateInformationEvent: (AppUpdateInformation) -> Unit,
    ) {
        val documentTask: Task<DocumentSnapshot> =
            Firebase.firestore.collection(BuildConfig.TEAM201_APP_VERSION_COLLECTION)
                .document(versionCode.toString())
                .get()
        documentTask.addOnSuccessListener { documentSnapshot ->
            onAppUpdateInformationEvent.invoke(
                documentSnapshot.toObject<AppUpdateInformation>()?.run {
                    copy(message = message.replace("\\n", "\n"))
                } ?: DEFAULT_APP_INFORMATION)
        }
        documentTask.addOnFailureListener { exception ->
            Log.d("Firestore Document", exception.message.toString())
            onAppUpdateInformationEvent.invoke(DEFAULT_APP_INFORMATION)
        }
    }

    companion object {
        private val DEFAULT_APP_INFORMATION = AppUpdateInformation(false, "")
    }
}
