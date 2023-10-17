package com.created.team201.data.repository

import android.util.Log
import com.created.domain.model.AppUpdateInformation
import com.created.domain.repository.SplashRepository
import com.created.team201.BuildConfig
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DefaultSplashRepository @Inject constructor() : SplashRepository {
    private val _appUpdateInformation: MutableSharedFlow<AppUpdateInformation> = MutableSharedFlow()
    override val appUpdateInformation: SharedFlow<AppUpdateInformation>
        get() = _appUpdateInformation.asSharedFlow()

    override suspend fun getAppUpdateInformation(versionCode: Int) {
        val documentTask =
            Firebase.firestore.collection(BuildConfig.TEAM201_APP_VERSION_COLLECTION)
                .document(versionCode.toString())
                .get()

        documentTask.addOnSuccessListener { documentSnapshot ->
            runBlocking {
                _appUpdateInformation.emit(
                    documentSnapshot.toObject<AppUpdateInformation>()?.run {
                        copy(message = message.replace("\\n", "\n"))
                    } ?: DEFAULT_APP_INFORMATION)
            }
        }
        documentTask.addOnFailureListener { exception ->
            Log.d("Firestore Document", exception.message.toString())
        }
    }

    companion object {
        private val DEFAULT_APP_INFORMATION = AppUpdateInformation(false, "")
    }
}
