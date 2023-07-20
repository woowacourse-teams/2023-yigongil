package com.created.team201.data.remote

import com.created.team201.data.remote.api.StudyListService

object NetworkServiceModule {

    val studyListService = NetworkModule.create<StudyListService>()
}
