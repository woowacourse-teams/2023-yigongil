package com.created.team201.presentation.studyManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.Round
import com.created.team201.presentation.studyManagement.model.StudyRoundsUiModel

class StudyManagementViewModel : ViewModel() {
    private val rounds: MutableLiveData<List<Round>> = MutableLiveData()
    private val _studyRounds: MutableLiveData<StudyRoundsUiModel> = MutableLiveData()
    val studyRounds: LiveData<StudyRoundsUiModel>
        get() = _studyRounds

    fun getRounds(studyId: Long) {
        rounds.value = listOf()
    }

    fun getStudyRounds(studyId: Long, currentRoundId: Long) {
        // getStudyRoundDetail()
        // _studyRounds.value = listOf()
    }

    private fun getStudyRoundDetail(studyId: Long, roundId: Long) {
        // 서버 통신으로 round 정보 가져옴
    }
}
