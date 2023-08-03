package com.created.team201.presentation.studyDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Member
import com.created.domain.model.Role
import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.datasource.remote.StudyDetailDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyDetailRepositoryImpl
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel
import kotlinx.coroutines.launch

class StudyDetailViewModel private constructor(
    private val studyDetailRepository: StudyDetailRepository,
) : ViewModel() {

    private val _study: MutableLiveData<StudyDetailUIModel> = MutableLiveData()
    val study: LiveData<StudyDetailUIModel> get() = _study
    private val _studyParticipants: MutableLiveData<List<StudyMemberUIModel>> = MutableLiveData()
    val studyParticipants: LiveData<List<StudyMemberUIModel>> get() = _studyParticipants
    private val _state: MutableLiveData<StudyDetailState> =
        MutableLiveData(StudyDetailState.Nothing)
    val state: LiveData<StudyDetailState> get() = _state

    fun fetchStudyDetail(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                val studyDetail = studyDetailRepository.getStudyDetail(studyId).toUIModel()
                studyDetail
            }.onSuccess {
                _study.value = it
                _studyParticipants.value = it.studyMembers
                _state.value = it.role.toStudyDetailState()
                if (it.role == Role.MASTER) fetchApplicants(studyId)
            }
        }
    }

    fun participateStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.participateStudy(studyId)
            }.onFailure { // 204 No Content가 onFailure로 가는 현상이 있습니다.
                _state.value = StudyDetailState.Applicant
            }
        }
    }

    private fun fetchApplicants(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getStudyApplicants(studyId)
            }.onSuccess { members ->
                _studyParticipants.value =
                    _studyParticipants.value?.plus(
                        members.map {
                            it.toUIModel(
                                study.value?.studyMasterId ?: 0L,
                                true,
                            )
                        },
                    )
            }
        }
    }

    fun startStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.startStudy(studyId)
            }.onFailure { // 204 No Content가 onFailure로 가는 현상이 있습니다.
                // 스터디가 성공적으로 시작된다면 시작후 화면으로 이동해야 합니다.
            }
        }
    }

    fun acceptApplicant(studyId: Long, memberId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.acceptApplicant(studyId, memberId)
            }.onFailure { // 204 No Content가 onFailure로 가는 현상이 있습니다.
                val studyParticipants = _studyParticipants.value ?: listOf()
                val acceptedMember =
                    studyParticipants.find { it.id == memberId } ?: StudyMemberUIModel.DUMMY
                _studyParticipants.value =
                    studyParticipants.minus(acceptedMember) + acceptedMember.copy(isApplicant = false)
            }
        }
    }

    private fun StudyDetail.toUIModel(): StudyDetailUIModel = StudyDetailUIModel(
        studyMasterId = studyMasterId,
        isMaster = role == Role.MASTER,
        title = this.name,
        introduction = this.introduction,
        peopleCount = this.numberOfMaximumMembers,
        role = this.role,
        startDate = this.startAt,
        period = this.totalRoundCount.toString(),
        cycle = this.periodOfRound,
        applicantCount = this.members.count(),
        studyMembers = this.members.map { it.toUIModel(this.studyMasterId, isApplicant = false) },
    )

    private fun Member.toUIModel(studyMasterId: Long, isApplicant: Boolean): StudyMemberUIModel =
        StudyMemberUIModel(
            id = id,
            isMaster = this.id == studyMasterId,
            isApplicant = isApplicant,
            profileImageUrl = this.profileImage,
            name = this.nickname,
            successRate = this.successRate.toInt(),
            tier = this.tier,
        )

    private fun Role.toStudyDetailState(): StudyDetailState = when (this) {
        Role.MASTER -> StudyDetailState.Master
        Role.MEMBER -> StudyDetailState.Member
        Role.APPLICANT -> StudyDetailState.Applicant
        Role.NOTHING -> StudyDetailState.Nothing
    }

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
