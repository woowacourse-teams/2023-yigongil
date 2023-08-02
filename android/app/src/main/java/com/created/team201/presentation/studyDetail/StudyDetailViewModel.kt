package com.created.team201.presentation.studyDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Member
import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.datasource.remote.StudyDetailDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyDetailRepositoryImpl
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel
import kotlinx.coroutines.launch

class StudyDetailViewModel(
    private val studyDetailRepository: StudyDetailRepository,
) : ViewModel() {

    private val _study: MutableLiveData<StudyDetailUIModel> = MutableLiveData()
    val study: LiveData<StudyDetailUIModel> get() = _study
    private val _studyParticipants: MutableLiveData<List<StudyMemberUIModel>> = MutableLiveData()
    val studyParticipants: LiveData<List<StudyMemberUIModel>> get() = _studyParticipants
    private val _isParticipateStudy: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isParticipateStudy: LiveData<Boolean> get() = _isParticipateStudy

    fun fetchStudyDetail(userId: Long, studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getStudyDetail(studyId).toUIModel(userId)
            }.onSuccess {
                _study.value = it
                _studyParticipants.value = it.studyMembers
                if (it.isMaster) fetchApplicants(1)
            }.onFailure {
                Log.d("StudyDetailViewModel", it.message.toString())
            }
        }
    }

    fun participateStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.participateStudy(studyId)
            }.onSuccess {
                _isParticipateStudy.value = true
            }.onFailure {
                _isParticipateStudy.value = false
            }
        }
    }

    fun startStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.startStudy(studyId)
            }.onSuccess {
                Log.d("bandal", "startStudy")
            }
        }
    }

    fun fetchApplicants(studyId: Long) {
        viewModelScope.launch {
            runCatching {
//                studyDetailRepository.getStudyApplicants(studyId)
            }.onSuccess {
                _studyParticipants.value = _studyParticipants.value?.plus(
                    listOf(
                        StudyMemberUIModel(
                            isMaster = false,
                            isApplicant = true,
                            tier = 3,
                            name = "bandal",
                            successRate = 90,
                            profileImageUrl = "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                        ),

                        StudyMemberUIModel(
                            isMaster = false,
                            isApplicant = true,
                            tier = 2,
                            name = "sunny",
                            successRate = 60,
                            profileImageUrl = "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
                        ),
                    ),
                )
            }
        }
    }

    private fun StudyDetail.toUIModel(userId: Long): StudyDetailUIModel = StudyDetailUIModel(
        isMaster = userId == studyMasterId,
        title = this.name,
        introduction = this.introduction,
        peopleCount = this.numberOfMaximumMembers,
        startDate = this.startAt,
        period = this.totalRoundCount.toString(),
        cycle = this.periodOfRound,
        applicantCount = this.members.count(),
        studyMembers = this.members.map { it.toUIModel(this.studyMasterId, isApplicant = false) },
    )

    private fun Member.toUIModel(studyMasterId: Long, isApplicant: Boolean): StudyMemberUIModel =
        StudyMemberUIModel(
            isMaster = this.id == studyMasterId,
            isApplicant = isApplicant,
            profileImageUrl = this.profileImage,
            name = this.nickname,
            successRate = this.successRate,
            tier = this.tier,
        )

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = StudyDetailRepositoryImpl(
                    StudyDetailDataSourceImpl(
                        NetworkServiceModule.studyDetailService,
                    ),
                )
                return StudyDetailViewModel(repository) as T
            }
        }
    }
}
