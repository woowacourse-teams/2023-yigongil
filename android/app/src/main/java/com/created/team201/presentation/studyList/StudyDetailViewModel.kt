package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.studyList.uiModel.StudyParticipant
import com.created.team201.presentation.studyList.uiModel.StudyUIModel

class StudyDetailViewModel : ViewModel() {
    private val _study: MutableLiveData<StudyUIModel> = MutableLiveData()
    val study: LiveData<StudyUIModel> get() = _study

    private val _studyParticipants: MutableLiveData<List<StudyParticipant>> = MutableLiveData()
    val studyParticipants: LiveData<List<StudyParticipant>> get() = _studyParticipants

    fun getStudyInformation() {
        _study.value = DUMMY_STUDY
        _studyParticipants.value = DUMMY_STUDY_PARTICIPANTS
    }

    companion object {
        val DUMMY_STUDY = StudyUIModel(
            title = "두 달 동안 자바 스터디 빡세게 하실 분 구합니다~!!",
            introduction = "안녕하세요 기깔나게 자바 스터디하실 분들 구합니다. 언제부터 할지는 미정인데요 미정이니? 어 잘지내? 음 .그렇구나 잘지내",
            peopleCount = 8,
            startDate = "2023.08.02",
            period = "8주",
            cycle = "1주",
            applicantCount = 3,
        )

        val DUMMY_STUDY_PARTICIPANTS = listOf(
            StudyParticipant(
                true,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "우아한반달",
                100,
                "diamond",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "98대장산군",
                75,
                "platinum",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "오른손해넷이",
                60,
                "gold",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "집에간써니",
                50,
                "silver",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "우아한반달",
                100,
                "bronze",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "98대장산군",
                75,
                "silver",
            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "오른손해넷이",
                60,
                "platinum",

            ),
            StudyParticipant(
                false,
                "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                "집에간써니",
                50,
                "diamond",
            ),
        )
    }
}
